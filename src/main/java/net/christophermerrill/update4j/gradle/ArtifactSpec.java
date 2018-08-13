package net.christophermerrill.update4j.gradle;

import java.util.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class ArtifactSpec
    {
    public String getFile()
        {
        return _file;
        }

    public void setFile(String file)
        {
        _file = file;
        }

    public String getPath()
        {
        return _path;
        }

    public void setPath(String path)
        {
        _path = path;
        }

    public static ArtifactSpec parse(String descriptor)
        {
        ArtifactSpec artifact = new ArtifactSpec();

        StringTokenizer tokenizer = new StringTokenizer(descriptor, ":");
        while (tokenizer.hasMoreTokens())
            {
            StringTokenizer params = new StringTokenizer(tokenizer.nextToken(), "=");
            switch (params.nextToken())
                {
                case "file":
                    artifact._file = params.nextToken();
                    break;
                case "path":
                    artifact._path = params.nextToken();
                    break;
                }
            }
        return artifact;
        }

    private String _file;
    private String _path;
    }


