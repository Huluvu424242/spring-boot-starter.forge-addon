package gh.funthomas424242.forge.addon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.inject.Default;
import javax.inject.Inject;

import org.jboss.forge.addon.maven.projects.MavenBuildSystem;
import org.jboss.forge.addon.parser.java.facets.JavaCompilerFacet;
import org.jboss.forge.addon.parser.java.facets.JavaSourceFacet;
import org.jboss.forge.addon.projects.Project;
import org.jboss.forge.addon.projects.ProjectFacet;
import org.jboss.forge.addon.projects.ProjectFactory;
import org.jboss.forge.addon.projects.facets.MetadataFacet;
import org.jboss.forge.addon.projects.facets.ResourcesFacet;
import org.jboss.forge.addon.resource.Resource;
import org.jboss.forge.addon.resource.ResourceFactory;
import org.jboss.forge.addon.ui.command.AbstractUICommand;
import org.jboss.forge.addon.ui.context.UIBuilder;
import org.jboss.forge.addon.ui.context.UIContext;
import org.jboss.forge.addon.ui.context.UIExecutionContext;
import org.jboss.forge.addon.ui.input.UIInput;
import org.jboss.forge.addon.ui.metadata.UICommandMetadata;
import org.jboss.forge.addon.ui.metadata.WithAttributes;
import org.jboss.forge.addon.ui.output.UIOutput;
import org.jboss.forge.addon.ui.result.Result;
import org.jboss.forge.addon.ui.result.Results;
import org.jboss.forge.addon.ui.util.Categories;
import org.jboss.forge.addon.ui.util.Metadata;

public class ProjectSetup extends AbstractUICommand {

	// public static final String PROJECT_CLASSIFIER = "testproject";

	@Inject
	protected ResourceFactory resourceFactory;

	@Inject
	protected ProjectFactory projectFactory;

	@Inject
	protected MavenBuildSystem buildSystem;

	///////////////////////////////////////////////////////////////////////////
	//
	// Definition of interactive inputs (parameters)
	//
	///////////////////////////////////////////////////////////////////////////
	
	@Inject
	@WithAttributes(label = "Bitten drücken Sie y und Bestätigen Sie mit Enter", required = true)
	// only to go into the interactive mode
	protected UIInput<String> seemless;

	
	@Inject
	@WithAttributes(label = "Group ID:", required = true, defaultValue="gh.funthomas424242.springboot")
	protected UIInput<String> groupId;

	
	@Inject
	@WithAttributes(label = "Artifact ID:", required = true, defaultValue="spring-boot-starter-specificname")
	protected UIInput<String> artifactId;

	@Inject
	@WithAttributes(label = "Version:", required = true, defaultValue="1.0.0-SNAPSHOT")
	protected UIInput<String> version;

	
	@Override
	public UICommandMetadata getMetadata(UIContext context) {
		return Metadata.forCommand(ProjectSetup.class)
				.name("create-spring-boot-starter-project")
				.category(Categories.create("Project"));
	}

	@Override
	public void initializeUI(UIBuilder builder) throws Exception {

		// add the inputs
		builder.add(seemless);
		builder.add(groupId);
		builder.add(artifactId);
		builder.add(version);
	}

	@Override
	public Result execute(UIExecutionContext context) throws Exception {


		final String projectGroupId=groupId.getValue();
		final String projectArtifactId=artifactId.getValue();
		final String projectVersion=version.getValue();
		
		final UIOutput log = context.getUIContext().getProvider().getOutput();
		log.info(log.out(),
				"Erstelle Projekt: " + projectArtifactId);
		final File dir = new File(projectArtifactId);
		dir.mkdirs();
		

		// AddonRegistry registry = ...
		// Imported<InputComponentFactory> imported =
		// registry.getServices(InputComponentFactory.class);
		// InputComponentFactory factory = imported.get();

		//System.out.println("Display UI: " + projectName);
		//final String newProjectName = projectName.getValue().toString();
		//System.out.println("Erstelle Projekt " + newProjectName);


		final Resource<File> projectDir = resourceFactory.create(dir);
		log.info(log.out(),"Verwende als Projektverzeichnis " + projectDir);

//		final DirectoryResource location = projectDir.reify(
//				DirectoryResource.class).getOrCreateChildDirectory("test2");
//		System.out.println("Location directory" + location);

		List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
		facets.add(ResourcesFacet.class);
		facets.add(MetadataFacet.class);
		facets.add(JavaSourceFacet.class);
		facets.add(JavaCompilerFacet.class);
		final Project project = projectFactory.createProject(projectDir, buildSystem,
				facets);
		
		
		final MetadataFacet metadata = project.getFacet(MetadataFacet.class);
		metadata.setProjectName(projectArtifactId);
		metadata.setProjectGroupName(projectGroupId);
		metadata.setProjectVersion(projectVersion);

		return Results
				.success("Command 'create-spring-boot-starter-project' successfully executed!");
	}

	// @Override
	// public void setupSimpleAddonProject(Project project, Version
	// forgeVersion) throws FileNotFoundException,
	// FacetNotFoundException {
	// generateReadme(project);
	// facetFactory.install(project, FurnaceVersionFacet.class);
	// facetFactory.install(project, ForgeVersionFacet.class);
	// project.getFacet(ForgeVersionFacet.class).setVersion(
	// forgeVersion.toString());
	//
	// facetFactory.install(project, ForgeBOMFacet.class);
	// facetFactory.install(project, FurnacePluginFacet.class);
	// facetFactory.install(project, AddonClassifierFacet.class);
	// facetFactory.install(project, JavaSourceFacet.class);
	// facetFactory.install(project, ResourcesFacet.class);
	// facetFactory.install(project, JavaCompilerFacet.class);
	// facetFactory.install(project, DefaultFurnaceContainerFacet.class);
	// facetFactory.install(project, CDIFacet_1_1.class);
	// facetFactory.install(project, AddonTestFacet.class);
	//
	// JavaSourceFacet javaSource = project.getFacet(JavaSourceFacet.class);
	// javaSource.saveJavaSource(Roaster.create(JavaPackageInfoSource.class)
	// .setPackage(javaSource.getBasePackage()));
	//
	// installSelectedAddons(project, dependencyAddons, false);
	// }
	//

	// private Project createProject(final Project parent, String moduleName,
	// String artifactId,
	// Class<? extends ProjectFacet>... requiredProjectFacets) {
	// DirectoryResource location = parent.getRoot()
	// .reify(DirectoryResource.class)
	// .getOrCreateChildDirectory(moduleName);
	//
	// List<Class<? extends ProjectFacet>> facets = new ArrayList<>();
	// facets.add(ResourcesFacet.class);
	// facets.addAll(Arrays.asList(requiredProjectFacets));
	// Project project = projectFactory.createProject(location, buildSystem,
	// facets);
	//
	// MetadataFacet metadata = project.getFacet(MetadataFacet.class);
	// metadata.setProjectName(artifactId);
	// return project;
	// }

	// /**
	// * @param project
	// */
	// protected void generateReadme(Project project) {
	// final String readmeTemplate = Streams.toString(getClass()
	// .getResourceAsStream("README.md"));
	// FileResource<?> child = project.getRoot()
	// .reify(DirectoryResource.class)
	// .getChildOfType(FileResource.class, "README.md");
	//
	// // TODO: Replace with template addon
	// MetadataFacet metadata = project.getFacet(MetadataFacet.class);
	// String content = readmeTemplate.replaceAll(
	// "\\{\\{ADDON_GROUP_ID\\}\\}", metadata.getProjectGroupName());
	// content = content.replaceAll(
	// "\\{\\{ADDON_ARTIFACT_ID\\}\\}", metadata.getProjectName());
	// child.createNewFile();
	// child.setContents(content);
	// }

}