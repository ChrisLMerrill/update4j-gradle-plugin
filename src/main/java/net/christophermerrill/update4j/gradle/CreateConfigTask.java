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
public class CreateConfigTask extends DefaultTask
    {
    @TaskAction
    void create() throws IOException
        {
System.out.println("create config!");
        Update4jGradleExtension extension = getProject().getExtensions().findByType(Update4jGradleExtension.class);
        if (extension == null)
            extension = new Update4jGradleExtension(getProject());

        prepareFolders(extension);

        Configuration.Builder builder = Configuration
            .withBase(URI.create(extension.getUri()), Paths.get(System.getProperty("user.dir")));

        /*
         * Add the libs for the launcher
         */
        File libs_folder = new File(_launcher_folder, "lib");
        File[] libs = libs_folder.listFiles();
        if (libs == null)
            {
            System.out.println("No files found in " + libs_folder.getAbsolutePath());
            return;
            }
        for (File lib : libs)
            builder = builder.library(Library.Reference
                .at(lib.getAbsolutePath())
                .path(Paths.get(String.format("lib/%s", lib.getName())))
                .build());

        /*
         * Add the bins for the launcher
         */
        File bins_folder = new File(_launcher_folder, "bin");
        File[] bins = bins_folder.listFiles();
        if (bins == null)
            {
            System.out.println("No files found in " + bins_folder.getAbsolutePath());
            return;
            }
        for (File bin : bins)
            builder = builder.library(Library.Reference
                .at(bin.getAbsolutePath())
                .path(String.format("/bin/%s", bin.getName()))
                .build());

        /*
         * Add the added assets of the app
         */
        if (_added_folder != null)
            {
            File[] added = _added_folder.listFiles();
            if (added == null)
                {
                System.out.println("No files found in " + _added_folder.getAbsolutePath());
                return;
                }
            String added_path = extension.getAddedAssetPath();
            if (added_path.length() > 0)
                 added_path = "/" + added_path;
            for (File asset : added)
                {
                builder = builder.library(Library.Reference
                    .at(asset.getAbsolutePath())
                    .path(String.format("%s/%s", added_path, asset.getName()))
                    .classpath()
                    .build());
                }
            }

        Configuration config = builder.build();

        File file = new File(_output_folder, "update-release.xml");
        System.out.println("writing to " + file.getCanonicalPath());
        FileOutputStream outstream = new FileOutputStream(file);
        config.write(new PrintWriter(outstream));
        outstream.close();
        }

    private void prepareFolders(Update4jGradleExtension extension) throws IOException
        {
        _added_folder = extension.resolveAdditionalLocation();
        if (_added_folder != null)
            if (!_added_folder.exists())
                throw new IOException("Extras location does not exist: " + _added_folder.getAbsolutePath());
        _launcher_folder = extension.resolveLauncherFolder();
        if (!_launcher_folder.exists())
            throw new IOException("Launcher folder does not exist: " + _launcher_folder.getAbsolutePath() + ". Did you run installDist?");
        _output_folder = extension.resolveOuptutLocation();
        if (!_output_folder.exists())
            if (!_output_folder.mkdir())
                throw new IOException("Cannot create output folder: " + _output_folder.getAbsolutePath());
        }

    private File _launcher_folder;
    private File _added_folder;
    private File _output_folder;
    }


