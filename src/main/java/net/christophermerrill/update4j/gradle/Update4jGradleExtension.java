package net.christophermerrill.update4j.gradle;

import org.gradle.api.*;

import java.io.*;

/**
 * The extension is the interface between build settings and the Plugin/Tasks
 *
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Update4jGradleExtension
    {
    public Update4jGradleExtension(Project project)
        {
        _project = project;
        }

    public String getAddedAssetLocation()
        {
        return _added_location;
        }

    public void setAddedAssetLocation(String additional_location)
        {
        _added_location = additional_location;
        }

    public File resolveAdditionalLocation()
        {
        if (_added_location != null)
            return new File(_added_location);
        return null;
        }

    public File resolveOuptutLocation()
        {
        if (_output_location != null)
            return new File(_output_location);
        return new File(_project.getBuildDir(), "update4j");
        }

    public String getOutputLocation()
        {
        return _output_location;
        }

    public void setOutputLocation(String output_location)
        {
        _output_location = output_location;
        }

    public String getLauncherFolder()
        {
        return _launcher_folder;
        }

    public void setLauncherFolder(String launcher_folder)
        {
        _launcher_folder = launcher_folder;
        }

    public File resolveLauncherFolder()
        {
        if (_launcher_folder != null)
            return new File(_launcher_folder);
        return new File(_project.getBuildDir(), "install/" + _project.getName());
        }

    public String getUri()
        {
        return _uri;
        }

    public void setUri(String uri)
        {
        _uri = uri;
        }

    public String getAddedAssetPath()
        {
        if (_added_path != null)
            return _added_path;
        return "";
        }

    public void setAddedAssetPath(String added_path)
        {
        _added_path = added_path;
        }

    private Project _project;

    private String _output_location;
    private String _launcher_folder;
    private String _uri;
    private String _added_location;
    private String _added_path;
    }
