# 🧪 Playground 

# Warning
None of this is up to date, I'm working on automating documentation, please stand by.

[![Google Cloud](https://img.shields.io/github/workflow/status/ashdavies/playground.ashdavies.dev/google-cloud?logo=github&logoColor=%23969da4)](https://github.com/ashdavies/playground.ashdavies.dev/actions/workflows/google-cloud.yaml)
[![Pre Merge](https://img.shields.io/github/workflow/status/ashdavies/playground.ashdavies.dev/pre-merge?logo=github&logoColor=%23969da4)](https://github.com/ashdavies/playground.ashdavies.dev/actions/workflows/pre-merge.yaml)
[![Renovate](https://img.shields.io/github/workflow/status/ashdavies/playground.ashdavies.dev/renovate?logo=github&logoColor=%23969da4)](https://github.com/ashdavies/playground.ashdavies.dev/actions/workflows/renovate.yaml)

[![Last Commit](https://img.shields.io/github/last-commit/ashdavies/playground.ashdavies.dev.svg)](https://github.com/ashdavies/playground.ashdavies.dev/commits/main)
[![Issues](https://img.shields.io/github/issues-pr/ashdavies/playground.ashdavies.dev.svg)](https://github.com/ashdavies/playground.ashdavies.dev/pulls)
[![License](https://img.shields.io/github/license/ashdavies/playground.ashdavies.dev.svg)](https://github.com/ashdavies/playground.ashdavies.dev/blob/main/LICENSE)

![Android 8.0.0](https://img.shields.io/badge/android-8.0.0-version.svg?colorA=555555&colorB=3DDC84&label=&logo=android&logoColor=ffffff&logoWidth=10)
![Compose 1.4.0](https://img.shields.io/badge/compose-1.4.0-version.svg?colorA=555555&colorB=4285F4&label=&logo=jetpack-compose&logoColor=ffffff&logoWidth=10)
![Kotlin 1.8.20](https://img.shields.io/badge/kotlin-1.8.20-version.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

## Projects

<table>
    <tr>
        <td>
            <h3>Cloud Run [cloud-run]</h3>
            <img src="https://img.shields.io/badge/project-experimental-status.svg?colorA=555555&colorB=red&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="Experimental" />
            <img src="https://img.shields.io/badge/project-jvm-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="JVM" />
        </td>
        <td>
            <h3>Dominion App [dominion-app]</h3>
            <img src="https://img.shields.io/badge/project-alpha-status.svg?colorA=555555&colorB=orange&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="Alpha" />
            <img src="https://img.shields.io/badge/project-desktop-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="Desktop" />
            <img src="https://img.shields.io/badge/project-android-component.svg?colorA=555555&colorB=3DDC84&label=&logo=android&logoColor=ffffff&logoWidth=10" alt="Android" />
        </td>
        <td>
            <h3>Events App [events-app]</h3>
            <img src="https://img.shields.io/badge/project-alpha-status.svg?colorA=555555&colorB=orange&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="Alpha" />
            <img src="https://img.shields.io/badge/project-desktop-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10" alt="Desktop" />
            <img src="https://img.shields.io/badge/project-android-component.svg?colorA=555555&colorB=3DDC84&label=&logo=android&logoColor=ffffff&logoWidth=10" alt="Android" />
            <p>Original multiplatform application to demonstrate the capabilities of the included Kotlin libraries and tools.</p>
            <p>Formerly exclusively for Android, but now configured for Desktop application, and iOS soon to follow.</p>
            <p>Application purpose is to view upcoming Android conferences, and allow collecting of virtual lanyards.</p>
        </td>
    </tr>
</table>

## Structure

```mermaid
stateDiagram-v2
    AppLauncher --> DominionApp
    AppLauncher --> EventsApp
    AppLauncher --> FirebaseCompose
    
    CloudRun --> AppCheckSdk
    CloudRun --> CloudFirestore
    CloudRun --> EventsAggregator
    CloudRun --> LocalRemote
    CloudRun --> LocalStorage
    
    DominionApp --> AppCheckClient
    DominionApp --> LocalRemote
    DominionApp --> PlaygroundApp
    
    EventsApp --> AppCheckClient
    EventsApp --> Auth0Auth
    EventsApp --> ComposeLocals
    EventsApp --> LocalRemote
    EventsApp --> LocalStorage
    EventsApp --> PlaygroundApp
```

## Modules

### App-Check [app-check]
![Planned](https://img.shields.io/badge/project-planned-status.svg?colorA=555555&colorB=blue&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![JVM](https://img.shields.io/badge/project-jvm-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

Implements a custom app-check provider for multiplatform applications to verify client applications.  
https://firebase.google.com/docs/app-check/custom-provider

### Auth Oauth [auth-oauth]
![Experimental](https://img.shields.io/badge/project-experimental-status.svg?colorA=555555&colorB=red&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![Multiplatform](https://img.shields.io/badge/project-multiplatform-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Build Plugins [build-plugins]
![Experimental](https://img.shields.io/badge/project-experimental-status.svg?colorA=555555&colorB=red&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![JVM](https://img.shields.io/badge/project-jvm-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Kotlin GB [kotlin-gb]
![Planned](https://img.shields.io/badge/project-planned-status.svg?colorA=555555&colorB=blue&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Local Storage [local-storage]
![Experimental](https://img.shields.io/badge/project-experimental-status.svg?colorA=555555&colorB=red&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![Multiplatform](https://img.shields.io/badge/project-multiplatform-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Notion Console [notion-console]
![Experimental](https://img.shields.io/badge/project-experimental-status.svg?colorA=555555&colorB=red&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![JVM](https://img.shields.io/badge/project-jvm-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Notion Import [notion-import]
![Planned](https://img.shields.io/badge/project-planned-status.svg?colorA=555555&colorB=blue&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![JVM](https://img.shields.io/badge/project-jvm-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)

### Playground App [playground-app]
![Deprecated](https://img.shields.io/badge/project-deprecated-status.svg?colorA=555555&colorB=grey&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)  
![Desktop](https://img.shields.io/badge/project-desktop-component.svg?colorA=555555&colorB=7F52FF&label=&logo=kotlin&logoColor=ffffff&logoWidth=10)
![Android](https://img.shields.io/badge/project-android-component.svg?colorA=555555&colorB=3DDC84&label=&logo=android&logoColor=ffffff&logoWidth=10)

## Talks
- Droidcon Berlin 2022
- Droidcon NYC 2019
- Droidcon Berlin 2018

## Releases

- [Droidcon NYC 2019: Implementing the Paging Library](https://github.com/ashdavies/playground/releases/tag/v3.0)
- [Droidcon Berlin 2018: Leveraging Android Data Binding with Kotlin](https://github.com/ashdavies/playground/releases/tag/v2.0)
- [Android Data Binding Beta: Sample](https://github.com/ashdavies/playground/releases/tag/v1.0)
