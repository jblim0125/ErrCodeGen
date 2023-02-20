package com.mobigen.platform.errcodegen;

import lombok.extern.slf4j.Slf4j;
import org.apache.maven.plugin.testing.AbstractMojoTestCase;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assumptions.assumeThatCode;

@Slf4j
public class ErrCodeGenMojoTest extends AbstractMojoTestCase {

    protected void setUp() throws Exception {
        // required for mojo lookups to work
        super.setUp();
    }

    public void testBasicUsageScenario() throws Exception {
        final File pom = getTestFile( "src/test/resources/pom.xml" );

        assumeThatCode( () -> lookupMojo( ErrCodeGenMojo.DEFAULT_GOAL, pom ).execute() )
                .doesNotThrowAnyException();

        assertThat(getTestFile("src/test/java/com/mobigen/platform/errcodegen/ResponseCode.java"))
                .hasSameContentAs(getTestFile("src/test/resources/ResponseCode.java"));
    }
}