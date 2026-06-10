# Senior Engineer — Expectations + Interview Process

Foundation reference. Two parts: (1) what Senior IS, (2) interview loop.

---

## Part 1 — Senior Expectations (Level doc)

**Senior Software Engineer = "Project technical leader."**
> Leads technical execution of projects, sets quality standards, guides peers, manages cross-team dependencies.

### Scope of Work
- Owns full projects or major portion of multi-engineer project
- **Independent technical judgment** across project lifecycle
- **Deep domain expertise**; begins influencing roadmap + architecture
- Coordinates contributors; navigates ambiguity with **minimal supervision**

### Engineering with AI
- Agentic AI across design, impl, testing, debugging, docs
- Holds outcomes to architectural, performance, maintainability standards
- Coordinates multiple agents; controls cost, reviewability
- AI for operational readiness (troubleshooting, monitoring)
- **Raises team AI effectiveness** — coaches validation, pilots shared standards

### Quality and Safety
- Owns quality + safety end-to-end; improves team practices
- Advanced tests for scalability, resiliency, safety-critical behavior
- Sets observability + deployment standards
- Leads incident resolution; authors design review / pre-mortem / after-action review

### Hiring and Developing Others
- Conducts interviews (system design and/or AI), calibrated feedback
- Mentors junior engineers ongoing
- Shares knowledge — grows peers

### Mid → Senior jump (the gap)
```
Mid    = Feature owner   — delivers complete features, judgment within a feature
Senior = Project leader  — owns whole project, sets standards, coordinates, navigates ambiguity solo
```

### Senior → Staff line (above target — don't over-reach)
```
Senior = owns project execution, influences arch
Staff  = owns the WHY, top-down arch, cross-team/org influence, defines scope itself
```

---

## Part 2 — Interview Process (Senior loop)

### System Design
| Field | Detail |
|---|---|
| Duration | 60 min |
| Interviewer | Any senior+ in system design pool |
| Tests | distributed systems architecture, backend tradeoffs, scalability, reliability, observability, maintainability, judgment |
| Note | **Fully technical. No behavioral / values questions.** |

### Engineering with AI
| Field | Detail |
|---|---|
| Duration | **90 min (longest block)** |
| Tests | AI-assisted backend engineering |
| First 45 min | requirements gathering, iteration, prompt definition, engineering choices, build WORKING solution |
| Second 45 min | deep inspection, debugging, critique, refinement at high bar |

### Engineering Leadership
| Field | Detail |
|---|---|
| Duration | 45 min |
| Interviewer | engineering people leader |
| Tests | deep dive into real project YOU led: judgment, scalability, operational rigor, quality bar, communication, values, **AI readiness** for senior scope |

### Behavioral / Values
| Field | Detail |
|---|---|
| Duration | 60 min |
| Tests | values, work history, leadership, collaboration, ownership, judgment, communication, behavioral evidence |

---

## Part 3 — Real interview evidence (anonymized, single data point — REFERENCE)

Candidate **passed Senior / not Staff**. Maps the boundary. NOTE: one panel, one domain — illustration, not universal bar.

| Interview | Result | Key reason |
|---|---|---|
| System Design | Senior | Strong execution; but bottom-up, jumped to sub-problem, couldn't hold big picture (= Staff fail) |
| Engineering w/ AI | **Fail** | Abandoned plan mode, no tests, SQL injection, no systematic review of AI output |
| Eng Leadership | Strong Senior | Owned a migration, RFCs, rollout — scope narrow for Staff |
| Behavioral | Senior not Staff | Didn't plan/coordinate at Staff scope; waited for alignment even when path obvious |

**What earned Senior:** real-scale execution, phased rollout (shadow → canary → 100%, observability), RFCs, owned project end-to-end, led small team, asks clarifying questions.

**Behavioral-interviewer weighting (authoritative):**
- **AI usage = additive, NOT deal-breaker** (bonus, won't sink alone)
- Real gates = **planning ahead**, **stakeholder management**, **scope/scale vs peers** (large scale hard to get — structural, acknowledged unfair)
- **Technical = table stakes** — fine, not the differentiator

---

## Part 4 — Synthesis (MY INFERENCE — not official, treat as hypothesis)

> Anchored in Parts 1-3 but weighting inferred from one interviewer's verbal note. Test against real signal; adjust. Do NOT trust as fact.

```
Coding correctness     → table stakes (filter, not differentiator)
Code review / security → evaluated hard (candidate sank AI interview here)
System design          → 60-min gate; must be TOP-DOWN (frame whole, then deep)
AI-assisted dev        → 90-min gate; additive to overall but biggest single block
Project ownership      → Senior clincher (Eng Leadership needs a real owned project)
Planning ahead         → real differentiator (the mid→senior gap)
Stakeholder/coordination → real differentiator, qualitative
Values                 → scored every interview
```
