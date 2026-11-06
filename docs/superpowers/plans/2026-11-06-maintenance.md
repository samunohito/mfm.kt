# Build maintenance implementation plan

Goal: Update Gradle, then Actions, then dependencies using the existing Renovate proposals.
Architecture: Keep the api/cli split and publication coordinates. Run Gradle on Java 21 and preserve Java 8 bytecode for the API and CLI.
Spec: User's delegated maintenance request; no push, publication, or merge without approval.

## Constraints and review focus
- Preserve parser behavior and API publication coordinates/version.
- Verify CLI serialization, documentation content, and generated Maven metadata.
- Use fixed stable plugin versions and a verified Gradle distribution checksum.
- Exercise tag and non-tag version extraction; keep release credentials scoped.
- Kotlin 2.4.21 officially supports Gradle through 9.7; test 9.8.2 explicitly and report this compatibility limitation.

## Tasks
- [x] 1. Upgrade wrapper to Renovate #16's Gradle 9.8.2. Update Kotlin to 2.4.21 and Shadow to the maintained com.gradleup.shadow 9.6.1 as required for Gradle 9. Replace obsolete version plugin with explicit version.properties loading if incompatible. Verify build, tests, shadowJar and Maven metadata.
- [x] 2. Adopt Renovate #23/#15/#13 action updates. Replace gradle-build-action with setup-gradle and normal wrapper commands, use Java 21, GITHUB_OUTPUT and job-scoped release permissions. Validate workflow YAML and version handling.
- [x] 3. Apply Renovate #8 Gson 2.15.0 and #19 Logback 1.6.6. Migrate Dokka to pinned 2.3.0 with API docs generation (#18). Verify clean build, all tests, shadowJar, docs and local Maven publication. Record superseded PRs #10/#17/#25 and remaining limitations.

Ruling: Execute in this task's dedicated clean cloud checkout on a maintenance branch; no additional worktree or approval pause is necessary for authorized local maintenance.


## Verification and rulings
- Clean build, dokkaHtml, and publishToMavenLocal passed with Gradle 9.8.2 / Java 21.0.13; 185 tests, no failures/errors/skips.
- Full/simple CLI JSON smoke tests passed after Gson update; API bytecode major 52 and publication metadata JVM 8 verified.
- Wrapper JAR SHA-256 matches Gradle's published checksum; distribution SHA-256 recorded in wrapper properties.
- actionlint 1.7.7 and version extraction checks (branch, PR, release, prerelease, invalid tag) passed.
- Independent whole-diff review: no actionable findings.
- Ruling: replace semantic-versioning plugin after reproducing removed Provider.forUseAtConfigurationTime failure; preserve version.properties and coordinates. Its unused bump tasks are no longer available.
- Ruling: migrate Dokka during build-tool upgrade because Gradle/plugin compatibility is coupled; explicitly select V2Enabled and retain dokkaHtml alias.
- Ruling: use scoped GITHUB_TOKEN instead of PAT; existing GitHub Packages must grant this repository write access (not verified remotely).
- Ruling: keep Java 8 bytecode; Kotlin metadata is now 2.4, requiring compatible Kotlin consumers. Java 8 runtime itself was not exercised.
- Known limitations: Kotlin's fully supported Gradle ceiling is 9.7 (9.8.2 passed locally); upstream Configuration.setVisible deprecation; external Dokka package-list downloads unavailable in this network; hosted Actions/release/Pages deployment untested.
- No remote push, PR creation, publication, or merge performed.
