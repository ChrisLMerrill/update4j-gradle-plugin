package net.christophermerrill.update4j.gradle;

import com.google.common.io.*;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.*;
import org.gradle.testkit.runner.*;
import org.junit.*;

import java.io.*;
import java.nio.charset.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
public class Tests
    {
    @Test
    public void uriRequired() throws IOException
        {
        createGradleFile(true, false, false);
        Assert.assertFalse(runBuild());
        }

    @Test
    public void pathRequired() throws IOException
        {
        createGradleFile(false, true, false);
        Assert.assertFalse(runBuild());
        }

    @Test
    public void defaultOutputLocation() throws IOException
        {
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        Assert.assertTrue(new File(_project, "build/" + Update4jGradleExtension.DEFAULT_OUTPUT_FILE).exists());
        }

    @Test
    public void customOutputLocation() throws IOException
        {
        createGradleFile(false, false, false);
        Assert.assertTrue(runBuild());
        Assert.assertTrue(new File(_project, "build/path/to/output.xml").exists());
        }

    @Test
    public void fileArtifacts() throws IOException
        {
        addBasicFileArtifacts();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"install/MyApp/lib/MyApp.jar\""));
        Assert.assertTrue(xml.contains("path=\"install/MyApp/lib/guava-23.0.jar\""));
        Assert.assertFalse(xml.contains("classpath"));
        }

    @Test
    public void fileArtifactWithClasspath() throws IOException
        {
        _files = "    file 'name=install/MyApp/lib/MyApp.jar|path=override/file.jar|classpath'\n";
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("classpath"));
        }

    @Test
    public void fileArtifactWithModulepath() throws IOException
        {
        _files = "    file 'name=install/MyApp/lib/MyApp.jar|path=override/file.jar|modulepath'\n";
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("modulepath"));
        }

    @Test
    public void fileArtifactAbsolute() throws IOException
        {
        _files = "    file 'name=" + new File(_project,"build/libs/MyApp.jar").getAbsolutePath().replace("\\", "\\\\") + "|path=absolute/MyApp.jar'\n";
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"absolute/MyApp.jar\""));
        }

    @Test
    public void fileArtifactsRelative() throws IOException
        {
        addFileArtifactsWithDefaultBasePath();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"lib/MyApp.jar\""));
        Assert.assertTrue(xml.contains("path=\"lib/guava-23.0.jar\""));
        }

    @Test
    public void fileArtifactsWithPathOverride() throws IOException
        {
        addFileArtifactWithPathOverride();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"override/file.jar\""));
        }

    @Test
    public void allFilesInFolder() throws IOException
        {
        addAllArtifactsInFolder();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertEquals(6, StringUtils.countMatches(xml, "<file path=\"install/MyApp/lib"));  // find the right number?
        Assert.assertTrue(xml.contains("path=\"install/MyApp/lib/MyApp.jar")); // verify one
        }

    @Test
    public void allFilesInFolderWithBaseOverride() throws IOException
        {
        addAllArtifactsInFolderWithBaseOverride();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertEquals(6, StringUtils.countMatches(xml, "<file path=\"lib"));  // find the right number?
        Assert.assertTrue(xml.contains("path=\"lib/MyApp.jar")); // verify one
        }

    @Test
    public void allFilesInFolderWithBaseOverrideAndPath() throws IOException
        {
        addAllArtifactsInFolderWithBaseOverrideAndPath();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertEquals(6, StringUtils.countMatches(xml, "<file path=\"mypath/"));  // find the right number?
        Assert.assertTrue(xml.contains("path=\"mypath/MyApp.jar")); // verify one
        }

    @Test
    public void userDirPath() throws IOException
        {
        _path = "'${user.dir}'";
        addBasicFileArtifacts();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"install/MyApp/lib/MyApp.jar\""));
        Assert.assertTrue(xml.contains("path=\"${user.dir}\""));
        }

    @Test
    public void userHomePath() throws IOException
        {
        _path = "'${user.home}'";
        addBasicFileArtifacts();
        createGradleFile(false, false, true);
        Assert.assertTrue(runBuild());
        String xml = getOutputFile();
        Assert.assertTrue(xml.contains("path=\"install/MyApp/lib/MyApp.jar\""));
        Assert.assertTrue(xml.contains("path=\"${user.home}\""));
        }

    private boolean runBuild()
        {
        try
            {
            BuildResult result = GradleRunner.create()
                .withProjectDir(_project)
                .withArguments("build", "installDist", "createUpdate4jConfig")
                .withPluginClasspath()
                .build();
            return true;
            }
        catch (Throwable e)
            {
            e.printStackTrace();
            System.out.println("build.gradle:");
            System.out.println("---------------------");
            System.out.println(_build_gradle);
            System.out.println("---------------------");
            return false;
            }
        }

    private void createGradleFile(boolean skip_uri, boolean skip_path, boolean skip_output) throws IOException
        {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        PrintStream printer = new PrintStream(bytes);
        printer.println("plugins {");
        printer.println("    id 'application'");
        printer.println("    id 'update4j-gradle-plugin'");
        printer.println("}");

        printer.println("repositories {");
        printer.println("    jcenter()");
        printer.println("}");

        printer.println("dependencies {");
        printer.println("    implementation 'com.google.guava:guava:23.0'");
        printer.println("}");

        printer.println("update4j {");
        if (!skip_uri)
            printer.println("    uri 'http://hostname.dom/update_path'");
        if (!skip_path)
            {
            String path = "System.properties['user.home']";
            if (_path != null)
                path = _path;
            printer.println("    path " + path);
            }
        if (!skip_output)
            printer.println("    output 'path/to/output.xml'");

        if (_files != null)
            printer.println(_files);

        printer.println("}");
        printer.println("mainClassName = 'mypackage.App'");
        printer.flush();
        printer.close();

        // save for diagnostics
        _build_gradle = bytes.toString(StandardCharsets.UTF_8);

        // write to file
        File build = new File(_project, "build.gradle");
        FileOutputStream outstream = new FileOutputStream(build);
        outstream.write(bytes.toByteArray());
        outstream.close();
        }

    private void addBasicFileArtifacts()
        {
        _files =
            "    file 'name=install/MyApp/lib/MyApp.jar'\n" +
            "    file 'name=install/MyApp/lib/guava-23.0.jar'\n";
        }

    private void addFileArtifactWithPathOverride()
        {
        _files =
            "    file 'name=install/MyApp/lib/guava-23.0.jar|path=override/file.jar'\n";
        }

    private void addFileArtifactsWithDefaultBasePath()
        {
        _files =
            "    defaultFolder 'install/MyApp/'\n" +
            "    file 'name=install/MyApp/lib/MyApp.jar'\n" +   // this one will resolve under the build path
            "    file 'name=lib/guava-23.0.jar'\n";             // this one will resolve under the specified base path
        }

    private void addAllArtifactsInFolder()
        {
        _files =
            "    folder 'name=install/MyApp/lib'\n";
        }

    private void addAllArtifactsInFolderWithBaseOverride()
        {
        _files =
            "    folder 'name=install/MyApp/lib|base=install/MyApp'\n";
        }

    private void addAllArtifactsInFolderWithBaseOverrideAndPath()
        {
        _files =
            "    folder 'name=install/MyApp/lib|base=install/MyApp/lib|path=mypath/'\n";
        }

    private String getOutputFile() throws IOException
        {
        return FileUtils.readFileToString(new File(_project, "build/update4j/config.xml"), Charset.defaultCharset());
        }

    @Before
    public void setupProject() throws IOException
        {
        _project = Files.createTempDir();
        FileUtils.copyDirectory(new File(getClass().getClassLoader().getResource("base_project").getFile()), _project);
        }

    @After
    public void teardown() throws IOException
        {
        FileUtils.deleteDirectory(_project);
        }

    private File _project;
    private String _build_gradle;

    private String _files = null;
    private String _path = null;
    }
