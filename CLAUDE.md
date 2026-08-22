# System Design Repository

Personal system-design study repo. Each top-level folder is one system, implemented for
educational purposes (mostly Java/Spring Boot, some Go). `Note/` holds standalone theory
notes (`.txt`) that are not tied to any single system.

## Repository conventions

Every system folder MUST contain:

| File | Purpose |
|---|---|
| `requirement.txt` | Raw design notes: functional + non-functional requirements, tradeoffs, open discussion. Free-form, written while thinking through the problem. |
| `README.md` | Polished summary that follows the standard template below. |
| `system.png` | Architecture diagram, referenced from the README. |

When adding a new system, create all three, then add a link to it under **List Systems**
in the root `README.md`.

## Standard README template

Every system `README.md` follows this shape. Keep the section names and order — the
Table of contents anchors depend on them. Sections that don't apply yet get
`To be Updated` rather than being deleted.

```markdown
## Table of contents
* [General info](#general-info)
* [Technologies](#technologies)
* [System Design](#system-design)
* [Set up](#set-up)
* [API Endpoint](#api-endpoint)
* [Testing Solution](#testing-solution)

## General info
**<System Name>**

A system aims to <one-line purpose>

## Technologies
* Java 17
* Springboot 3.1.10
* <other tech, one per line>

## System Design
![System Design](system.png)

## Set up
* <how to run it, usually docker-compose>

## API Endpoint
|Endpoint|Method|Description|
|---|---|---|
|/v1/example|GET|What it does|

## Testing Solution
* Unit Test - JUnit 5
* Integration Test - JUnit 5 and TestContainers
* Load Test - JMeter
```

Notes on the template:

- The System Design image uses a **relative path** (`system.png`), so it renders on
  GitHub, in local editors, and on any branch or fork. Do not use a
  `github.com/.../blob/main/...` URL — that serves HTML, so GitHub's image proxy shows a
  broken image. If an absolute URL is ever needed, use `raw.githubusercontent.com`.
- Extra sections are allowed for systems with more depth (see `twitch-system`, which adds
  *End-to-end flow*, *Project Layout*, and *Notes / Deviations*). Add them to the Table of
  contents and keep them after the core sections; never drop a core section to make room.
- Systems that are diagram-only may use the short form: Table of contents with just
  *General info* and *System design*, plus those two sections.

## Requirement notes convention

`requirement.txt` is a thinking document, not a spec. The usual shape:

```
Design <System>
Functional Requirement
1/ ...
Non-Function Requirement
1/ ...
Open discussion
1/ ...
```

Tradeoffs are written inline with `->` for consequences and `+`/`-` for options. Keep it
raw — it records the reasoning, and the README records the outcome.

## Current gaps

- Missing `requirement.txt`: `proximity-system`, `url-shortener-system`, `youtube-system`,
  `consistent-hashing`, `locker-service`.
- Missing `README.md`: `rate-limiter`, `consistent-hashing`, `locker-service`.
- Missing `system.png`: `rate-limiter`, `consistent-hashing`, `locker-service`.
- `consistent-hashing` is code + notes only (`ConsistentHashing.java`, `.txt`);
  `locker-service` holds screenshots only. Both still need the standard three files.
- Root `README.md` **List Systems** does not link `twitch-system`, `consistent-hashing`,
  or `locker-service`.
