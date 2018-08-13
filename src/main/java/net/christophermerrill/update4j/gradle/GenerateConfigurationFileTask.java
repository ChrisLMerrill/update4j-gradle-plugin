package net.christophermerrill.update4j.gradle;

import org.gradle.api.*;
import org.gradle.api.tasks.*;
import org.update4j.*;

import java.io.*;
import java.net.*;
import java.nio.file.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class GenerateConfigurationFileTask extends DefaultTask
    {
    @TaskAction
    public void create() throws IOException
        {
        Update4jGradleExtension extension = getProject().getExtensions().findByType(Update4jGradleExtension.class);

        String url = extension.getUri();
        if (url == null)
            throw new IllegalStateException("The 'uri' property must be provided in a 'update4j' block. This property must specify the base URL of the files to retrieve for the update.");

        String path = extension.getPath();
        if (path == null)
            throw new IllegalStateException("The 'path' property must be provided in a 'update4j' block. This property must specify the base path of the files to update (in the installation).");


        Configuration.Builder builder = Configuration
            .withBase(URI.create(extension.getUri()), Paths.get(extension.getPath()));

        File build_dir = getProject().getBuildDir();
        File base_dir = build_dir;
        if (extension.getArtifactDefaultFolder() != null)
            base_dir = new File(getProject().getBuildDir(), extension.getArtifactDefaultFolder());
        Path base_path = base_dir.toPath().normalize();
        for (ArtifactSpec spec : extension.getArtifacts())
            {
            File artifact_file = new File(build_dir, spec.getFile());
            if (!artifact_file.exists() && new File(base_dir, spec.getFile()).exists())
                artifact_file = new File(base_dir, spec.getFile());
            Path artifact_path = artifact_file.toPath();
            Path relative_path = base_path.relativize(artifact_path);
            if (spec.getPath() != null)
                relative_path = Paths.get(spec.getPath());
            builder = builder.library(Library.Reference.at(artifact_file.getAbsolutePath()).path(relative_path).build());
            }

        Configuration config = builder.build();

        File file = extension.resolveOuptutFile();
        file.getParentFile().mkdirs();
        System.out.println("writing to " + file.getCanonicalPath());
        FileOutputStream outstream = new FileOutputStream(file);
        config.write(new PrintWriter(outstream));
        outstream.close();
        }
    }


