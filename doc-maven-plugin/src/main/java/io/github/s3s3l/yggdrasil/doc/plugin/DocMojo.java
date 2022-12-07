package io.github.s3s3l.yggdrasil.doc.plugin;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import io.github.s3s3l.yggdrasil.utils.reflect.scan.ClassScanner;

/**
 * <p>
 * </p>
 * Date: Sep 27, 2019 4:10:28 PM <br>
 * 
 * @author kehw_zwei
 * @version 1.0.0
 * @since JDK 1.8
 */
@Mojo(name = "doc")
public class DocMojo extends AbstractMojo {

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        System.out.println("hello mojo");
        new ClassScanner().scan("io.github.s3s3l.yggdrasil.doc")
                .forEach(System.out::println);
    }

}
