package net.jhorstmann.propertychangesupport;

import java.io.File;
import java.io.IOException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * @requiresDependencyResolution compile
 * @goal transform
 * @phase process-classes
 */
public class TransformClassesMojo extends AbstractMojo {
    /**
     * @parameter expression="${project.build.outputDirectory}"
     * @required
     */
    private File outputDir;

    public void execute() throws MojoExecutionException, MojoFailureException {
        try {
            Transform.transformRecursive(outputDir);
        } catch (IOException ex) {
            throw new MojoExecutionException(ex.getClass().getSimpleName() + " in " + getClass().getSimpleName(), ex);
        }
    }

}
