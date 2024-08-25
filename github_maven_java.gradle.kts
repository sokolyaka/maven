tasks.register<Jar>("sourcesJar") {
    dependsOn("classes")
    from(sourceSets["main"].allSource)
}

tasks.register<Jar>("javadocJar") {
    dependsOn("javadoc")
    from(tasks["javadoc"].outputs.files)
}

artifacts {
    add("archives", tasks["sourcesJar"])
    add("archives", tasks["javadocJar"])
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            groupId = publishedGroupId
            artifactId = publishedArtifactId
            version = publishedVersion
            from(components["java"])
            artifact(tasks["sourcesJar"])
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/$GITHUB_USERID/$REPOSITORY")
            credentials {
                username = System.getenv("GPR_USER") ?: project.findProperty("gpr.usr") as String?
                password = System.getenv("GPR_API_KEY") ?: project.findProperty("gpr.key") as String?
            }
        }
    }
}
