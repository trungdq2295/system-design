# Core Insights — what Senior actually is

Distilled from analyzing real interview evidence + interviewer weighting.

## The one-liner

```
Senior = own a real project, decide it from the business WHY,
         drive it done, prove impact with numbers.
```

## The core skill: two-way map business ↔ technical

```
Business → Technical:  "company needs X" → "so I build Y"   (pick the RIGHT work)
Technical → Business:  "I built Y"        → "gave X impact"  (prove it mattered)
```

Hold the WHOLE chain:
```
business problem → WHY it matters → technical decision → execution →
measurable impact → back to business value
```
Junior holds the middle (build Y). Senior holds the ends too (why + impact).

## What separates Senior from Mid (the real gap)

Not coding — coding is table stakes. The gap:
1. **Map business ↔ tech** (the lens)
2. **Decide** what to build (own the choice, not assigned)
3. **Drive** it done (coordinate, ship, FINISH)
4. **Own the outcome** (the metric, good or bad)

## Output, not process

AI/engineering judged on OUTPUT — but "output" = production-grade + defensible:
```
1. Works            (juniors stop here)
2. Correct + safe   (tests, security, edge cases)
3. Defensible       (explain WHY, fix under attack)
```
Bar needs layer 3. Process (spec/tests/review) is just how you guarantee layer 3.

## Don't be a "learner" — be a real engineer

```
Learner   → study interview, fake stories, grind prep
Real eng  → own real projects, record real metrics, story writes itself
```
Best prep = real owned work with tracked before/after numbers. Interview = report truth.

## Migration project = great Senior material

Real ownership, real rollout, natural before/after metrics. Traps:
- Don't lead with morale — lead with hard business cost, morale as a pillar WITH a number
- Retention IS a business case → quantify it (attrition cost = ~6-12mo salary/eng to replace)
- #1 failure: never finishes. Small migration FULLY done > big migration 20% done
- Own the DECISIONS (why migrate, which stack, strategy, rollout) — else you're executor not owner

## Business framing = the senior tell

Take the "soft" thing everyone feels → attach a number → make it undeniable.
```
WEAK   "legacy painful, engineers don't like it"
STRONG "legacy costs X hrs/mo maintenance, Y incidents/mo, 2 engineers quit
        citing stack ($Z to replace), N features blocked. Migration ROI: pays back in M months."
```
Every project: know the business reason + the metric that proves it.
```
Junior:  "I migrated to new stack"
Senior:  "I migrated → cut p99 40%, unblocked 3 teams, saved $X/mo"
```
Same work. Senior frames impact. That IS the jump.

## Operating where there's no process (or anti-process "move fast")

Process names (design review, pre-mortem, AAR) = company labels. Substance is universal — DO it anywhere, no company needs to "have" it:
```
Design Review  →  design + tradeoffs written before building
Pre-Mortem     →  list what could break before shipping
AAR            →  after incident, write what happened + fix
Phased rollout →  shadow → canary → full + monitoring
```

**No process (immature company):** vacuum → you fill it → welcomed. Setting standards IS the senior bar ("improves team practices"). Can't do this where process already exists.

**Anti-process ("move fast" culture):** worst case. Culture fights rigor as "slow." But "move fast" ≠ "no rigor" — that's the lie:
```
Fake fast:  skip tests/design → ship → break prod → firefight → SLOWER
Real fast:  lightweight rigor → ship confident → don't revisit → FASTER
```

How senior operates in move-fast: smuggle rigor in LIGHTWEIGHT, framed as speed.
```
Heavy (rejected)        Lightweight (accepted)
10-page design doc   →  1-paragraph + diagram in PR
formal pre-mortem    →  "what breaks?" 3 bullets in PR
full test suite      →  test the risky path only
staged rollout board →  feature flag + watch dashboard 10min
```
Frame in their language: not "we need process for quality" (sounds slow) but "feature flag = ship today, rollback in 1 click" (sounds fast).

Payoff: chaos around you → incidents. Your rigor → your services don't break → metrics prove it: "team 5 incidents/mo, my services 0." Contrast = undeniable senior signal. Discipline can't depend on company process — it's YOURS.
