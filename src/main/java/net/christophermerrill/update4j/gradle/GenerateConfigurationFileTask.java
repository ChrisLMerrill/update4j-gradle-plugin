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

        builder = addArtifacts(extension, builder);
        builder = addArtifactFolders(extension, builder);

        Configuration config = builder.build();

        File file = extension.resolveOuptutFile();
        file.getParentFile().mkdirs();
        FileOutputStream outstream = new FileOutputStream(file);
        PrintWriter printer = new PrintWriter(outstream);
        writeExtra(printer);
        config.write(printer);
        outstream.close();
        }

    private Configuration.Builder addArtifacts(Update4jGradleExtension extension, Configuration.Builder builder)
        {
        File build_dir = getProject().getBuildDir();
        File base_dir = build_dir;
        if (extension.getArtifactDefaultFolder() != null)
            base_dir = new File(getProject().getBuildDir(), extension.getArtifactDefaultFolder());
        Path base_path = base_dir.toPath().normalize();
        for (ArtifactSpec spec : extension.getArtifactList())
            {
            File artifact_file = new File(build_dir, spec.getFile());
            if (!artifact_file.exists() && new File(base_dir, spec.getFile()).exists())
                artifact_file = new File(base_dir, spec.getFile());
            if (!artifact_file.exists())
                throw new IllegalArgumentException("The specified artifact file does not exist: " + artifact_file.getAbsolutePath());
            Path artifact_path = artifact_file.toPath();
            Path relative_path = base_path.relativize(artifact_path);
            if (spec.getPath() != null)
                relative_path = Paths.get(spec.getPath());
            builder = builder.library(Library.Reference.at(artifact_file.getAbsolutePath()).path(relative_path).build());
            }
        return builder;
        }

    private Configuration.Builder addArtifactFolders(Update4jGradleExtension extension, Configuration.Builder builder)
        {
        File build_dir = getProject().getBuildDir();
        File base_dir = build_dir;
        if (extension.getArtifactDefaultFolder() != null)
            base_dir = new File(getProject().getBuildDir(), extension.getArtifactDefaultFolder());
        Path base_path = base_dir.toPath().normalize();
        for (ArtifactFolderSpec spec : extension.getArtifactFolderList())
            {
            File folder = new File(build_dir, spec.getFolder());
            if (!folder.exists())
                throw new IllegalArgumentException("The specified artifact folder does not exist: " + folder.getAbsolutePath());
            if (!folder.isDirectory())
                throw new IllegalArgumentException("The specified folder param does not specify a folder: " + folder.getAbsolutePath());
            File[] files = folder.listFiles();
            if (files == null)
                throw new IllegalArgumentException("The specified folder is empty: " + folder.getAbsolutePath());

            Path folder_base_path = base_path;
            if (spec.getBase() != null)
                folder_base_path = new File(build_dir, spec.getBase()).toPath().normalize();
debug(String.format("folder_base_path=%s\n", folder_base_path));

            for (File artifact_file : files)
                {
                if (!Files.isRegularFile(artifact_file.toPath()))
                    continue;
                Path artifact_path = artifact_file.toPath().normalize();
                Path relative_path = folder_base_path.relativize(artifact_path);
debug(String.format("relative_path=%s\n", relative_path));
                builder = builder.library(Library.Reference.at(artifact_file.getAbsolutePath()).path(relative_path).build());
                }
            }
        return builder;
        }

    private void debug(String message)
        {
        _extra_info.append(message);
        System.err.print(message);
        }

    private void writeExtra(PrintWriter printer)
        {
        String extra = _extra_info.toString();
        if (extra.length() > 0)
            {
            printer.println("<!--");
            printer.println(extra);
            printer.println("-->");
            }

        }

    private StringBuilder _extra_info = new StringBuilder();
    }