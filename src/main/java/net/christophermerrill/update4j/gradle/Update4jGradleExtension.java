package net.christophermerrill.update4j.gradle;

import groovy.lang.*;
import org.gradle.api.*;

import java.io.*;
import java.util.*;

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

    public String getUri()
        {
        return _uri;
        }

    public void setUri(String uri)
        {
        _uri = uri;
        }

    public String getPath()
        {
        return _path;
        }

    public void setPath(String path)
        {
        _path = path;
        }

    public String getOutput()
        {
        return _output_file;
        }

    public void setOutput(String output_file)
        {
System.out.println("output file is " + output_file);
        _output_file = output_file;
        }

    File resolveOuptutFile()
        {
        return new File(_project.getBuildDir(), Objects.requireNonNullElse(_output_file, DEFAULT_OUTPUT_FILE));
        }

/*
    public void setFile(Object target)
        {
System.out.println(String.format("received a %s: %s  ", target.getClass().getSimpleName(), target));
        }
*/


//    public void setUpdateFile(String file)
//        {
//System.out.println(String.format("Received file %s", file));
//        }


    public String getArtifact()
        {
        return _artifacts.get(0).toString();
        }

    public void setArtifact(String descriptor)
        {
System.out.println("adding artifact descriptor =" + descriptor);
        ArtifactSpec artifact = ArtifactSpec.parse(descriptor);
        _artifacts.add(artifact);
        }

    public List<ArtifactSpec> getArtifacts()
        {
        return _artifacts;
        }

    public String getArtifactDefaultFolder()
        {
        return _artifact_default_folder;
        }

    public void setArtifactDefaultFolder(String artifact_base_path)
        {
        _artifact_default_folder = artifact_base_path;
        }

    private Project _project;

    // current
    private String _uri = null;
    private String _path = null;
    private String _output_file = null;
    private String _artifact_default_folder = null;
    private List<ArtifactSpec> _artifacts = new ArrayList<>();

    public final static String DEFAULT_OUTPUT_FILE = "update4j/config.xml";

    /**
     * Obsolete stuff
     */
    private String _output_location;
    private String _launcher_folder;
    private String _added_location;
    private String _added_path;

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

    public String getAddedAssetLocation()
        {
        return _added_location;
        }

    public void setAddedAssetLocation(String additional_location)
        {
        _added_location = additional_location;
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

    }
