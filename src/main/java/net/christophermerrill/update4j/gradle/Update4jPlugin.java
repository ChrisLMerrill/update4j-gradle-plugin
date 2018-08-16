package net.christophermerrill.update4j.gradle;

import org.gradle.api.*;

/**
 * @author Christopher L Merrill (see LICENSE.txt for license details)
 */
@SuppressWarnings("unused") // discovered via reflection/publication
public class Update4jPlugin implements Plugin<Project>
    {
    @Override
    public void apply(Project project)
        {
        project.getExtensions().create("update4j", Update4jGradleExtension.class, project);
        project.getTasks().create("createUpdate4jConfig", GenerateConfigurationFileTask.class);
        }
    }
