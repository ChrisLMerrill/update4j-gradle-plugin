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

        Configuration.Builder builder = Configuration.builder().baseUri(URI.create(extension.getUri())).basePath(computePath(extension));

        builder = addFiles(extension, builder);
        builder = addFolders(extension, builder);

        Configuration config = builder.build();

        File file = extension.resolveOuptutFile();
        file.getParentFile().mkdirs();
        FileOutputStream outstream = new FileOutputStream(file);
        PrintWriter printer = new PrintWriter(outstream);
        writeExtra(printer);
        config.write(printer);
        printer.flush();
        outstream.flush();
        outstream.close();
        }

    private Path computePath(Update4jGradleExtension extension)
        {
        String path = extension.getPath();
        path = path.replace("${user.dir}", System.getProperty("user.dir"));
        path = path.replace("${user.home}", System.getProperty("user.home"));
        return Paths.get(path);
        }

    private Configuration.Builder addFiles(Update4jGradleExtension extension, Configuration.Builder builder)
        {
        File build_dir = getProject().getBuildDir();
        File base_dir = build_dir;
        if (extension.getArtifactDefaultFolder() != null)
            base_dir = new File(getProject().getBuildDir(), extension.getArtifactDefaultFolder());
        Path base_path = base_dir.toPath().normalize();
        for (FileSpec spec : extension.getArtifactList())
            {
            File file = new File(spec.getName());
            if (!file.isAbsolute())
                file = new File(build_dir, spec.getName());
            if (!file.exists() && new File(base_dir, spec.getName()).exists())
                file = new File(base_dir, spec.getName());
            if (!file.exists())
                throw new IllegalArgumentException("The specified artifact file does not exist: " + file.getAbsolutePath());
            Path artifact_path = file.toPath();
            Path relative_path = base_path.relativize(artifact_path);
            if (spec.getPath() != null)
                relative_path = Paths.get(spec.getPath());
            builder = builder.file(FileMetadata.readFrom(file.getAbsolutePath()).path(relative_path).classpath(spec.isClasspath()).modulepath(spec.isModulepath()));
            }
        return builder;
        }

    private Configuration.Builder addFolders(Update4jGradleExtension extension, Configuration.Builder builder)
        {
        File build_dir = getProject().getBuildDir();
        File base_dir = build_dir;
        if (extension.getArtifactDefaultFolder() != null)
            base_dir = new File(getProject().getBuildDir(), extension.getArtifactDefaultFolder());
        Path base_path = base_dir.toPath().normalize();
        for (FolderSpec spec : extension.getArtifactFolderList())
            {
            File folder = new File(build_dir, spec.getName());
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
            //debug(String.format("folder_base_path=%s\n", folder_base_path));

            for (File artifact_file : files)
                {
                if (!Files.isRegularFile(artifact_file.toPath()))
                    continue;
                Path artifact_path = artifact_file.toPath().normalize();
                Path relative_path = folder_base_path.relativize(artifact_path);
                if (spec.getPath() != null)
                    relative_path = Paths.get(spec.getPath() + artifact_file.getName());
                //debug(String.format("relative_path=%s\n", relative_path));
                builder = builder.file(FileMetadata.readFrom(artifact_file.getAbsolutePath()).path(relative_path).classpath(spec.isClasspath()).modulepath(spec.isModulepath()));
                }
            }
        return builder;
        }

    /**
     * Useful for getting debug messages from the task.
     */
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