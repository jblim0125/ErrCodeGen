package com.mobigen.platform.errcodegen;


import com.google.common.collect.ImmutableMap;
import com.mobigen.platform.errcodegen.binding.ConfigModel;
import freemarker.cache.FileTemplateLoader;
import freemarker.template.*;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Mojo(name = ErrCodeGenMojo.DEFAULT_GOAL, threadSafe = true)
public class ErrCodeGenMojo extends AbstractMojo {

    public static final String DEFAULT_GOAL = "generate";
    public static final Charset DEFAULT_ENCODING = StandardCharsets.UTF_8;
    public static final Version DEFAULT_FREEMARKER_VERSION = Configuration.VERSION_2_3_32;

    @Parameter(name = "input", required = true)
    private String input;

    @Parameter(name = "output", required = true)
    private String output;

    private ConfigModel config = new ConfigModel();

    public void execute() throws MojoExecutionException {
        final StopWatch started = StopWatch.createStarted();

        getLog().info( "Input " + input );
        getLog().info( "Output " + output );
        config.setInput( input );
        config.setOutput( output );

        validatedDeclaredConfig();

        try {
            final File messageFile = new File( config.getInput() );
            final Map<String, String> errCodeNMsg = loadErrCodeAndMessage( messageFile );

            getLog().debug( "Preparing FreeMarker to render " + messageFile );

            final File templateFile = new File( "src/main/resources/template.ftl" );

            final Configuration cfg = new Configuration( DEFAULT_FREEMARKER_VERSION );
            cfg.setDefaultEncoding( DEFAULT_ENCODING.displayName() );
            cfg.setTemplateExceptionHandler( TemplateExceptionHandler.RETHROW_HANDLER );
            cfg.setLogTemplateExceptions( false );
            cfg.setTemplateLoader( new FileTemplateLoader( templateFile.getParentFile() ) );

            final Template template = cfg.getTemplate( templateFile.getName() );


            final File outputDst = new File( config.getOutput() );
            try( final Writer writer = prepareDestinationForWrite( outputDst ) ) {
                final Map<String, Object> root = new HashMap<>();

                root.put( "data",
                        ImmutableMap.of(
                                "content", errCodeNMsg,
                                "output", outputDst.getName()
                        )
                );

                template.process( root, writer );
                writer.flush();
                getLog().info( "Processed  " + messageFile + " to " + outputDst );
            }
            started.stop();
            getLog().info( "Processed in " + started.getTime( TimeUnit.MILLISECONDS ) + " in ms." );
        } catch( IOException | TemplateException e ) {
            throw new MojoExecutionException( e.getMessage(), e );
        }
    }

    private void validatedDeclaredConfig() throws MojoExecutionException {
        if( !config.check() ) {
            throw new MojoExecutionException( "No models configured. Please, either add one or remove plugin declaration." );
        }
    }

    private Map<String, String> loadErrCodeAndMessage( final File messageFile ) throws IOException {
        getLog().debug( "Loading ErrCode And Message From " + messageFile );
        try( final FileInputStream io = new FileInputStream( messageFile ) ) {
            return new Yaml().load( io );
        }
    }

    private Writer prepareDestinationForWrite( final File outputDst ) throws IOException {
        final File outDstParent = outputDst.getParentFile();
        if( !outDstParent.exists() ) {
            outDstParent.mkdirs();
        }
        return new FileWriter( outputDst );
    }
}