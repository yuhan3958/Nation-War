You are an expert Minecraft Forge 1.20.1 mod architect and senior build engineer.

Your task: produce a detailed, implementation-ready task list (no code) for a nation-based war server mod ecosystem.
The output must be a structured hierarchy of tasks with clear dependencies, ordered for execution.

================================
WORKSPACE / TOOLING (MANDATORY)
================================

Development environment:
- Use IntelliJ IDEA as the primary IDE.
- Single Git repository.
- Gradle multi-module project (NOT multiple separate repos).
- Target Minecraft Forge 1.20.1.
- Both-side mods: include client + server code where specified.
- Use Java (and/or Kotlin only if explicitly required; default to Java).

AI workflow requirement:
- Include tasks to set up an AI-assisted workflow using Gemini CLI (preferred) and optionally IDE plugin.
- The task list must include a practical workflow plan: “small edits in IDE, large refactors via CLI”.

================================
ARCHITECTURE OVERVIEW
================================

There are FOUR top-level mods:
1) Core
2) Nation
3) Tech
4) Ore

Rules:
- Core must not depend on Nation/Tech/Ore.
- Nation/Tech/Ore may depend on Core.
- Nation/Tech/Ore should not hard-depend on each other unless explicitly stated.
- Client GUI framework is owned by Core-client; other mods register UI elements via interfaces.

Required third-party mods to interop with:
- Create (Create-family)
- GregTech CEu Modern
- Tinkers’ Construct
- TaCZ
  Optional server integration:
- Dynmap integration (server-side only; optional at runtime)

Dimensions:
- Nether and End are NOT claimable.
- Overworld is claimable.
- Ad Astra dimensions (Moon, Mars, etc.) ARE claimable.
- Dimension mapping must be configurable, not hardcoded.

================================
PROJECT STRUCTURE (MANDATORY)
================================

Implement as ONE repository with Gradle multi-modules.

Required module layout (recommended naming; adjust but keep separation):
- core-common, core-client, core-server (Core mod outputs a both-side jar; internal split by source sets or submodules)
- nation-common, nation-client, nation-server
- tech-common, tech-client, tech-server
- ore-common, ore-server (ore-client optional if later needed)

Deliverables:
- Support building:
  A) separate jars for Core/Nation/Tech/Ore (recommended for server deployment)
  B) optionally an “all-in-one” dev build artifact for quick testing

================================
CORE MOD (HIGHEST PRIORITY)
================================

Core is BOTH-SIDE (client + server). Core defines shared APIs, contracts, and utilities.
Core must remain domain-agnostic (no nation logic, no tech logic, no ore/worldgen logic).

-----------------------------
Core - Shared API (core-common)
-----------------------------
Provide:
1) ActionType enum:
- BLOCK_BREAK, BLOCK_PLACE, BLOCK_USE, CONTAINER_OPEN
- ENTITY_DAMAGE, PROJECTILE_IMPACT
- EXPLOSION_BLOCK_DAMAGE, FLUID_USE, FIRE_IGNITE
- ITEM_CRAFT, ITEM_SMELT, MACHINE_OUTPUT (for tech enforcement)

2) Context models:
- actor (optional), level/dimension, position, target entity, item involved, source metadata

3) Guard interface:
- returns ALLOW / DENY / PASS + standardized reason code + optional message key

4) GuardBus pipeline:
- supports priority ordering and short-circuit:
    - DENY aborts immediately
    - PASS continues
    - ALLOW is provisional and can still be overridden by later DENY
- Must allow external modules (Nation/Tech) to register guards at runtime

5) Deny feedback contract:
- standardized payload for client display (action type, reason, optional owner label)

6) Audit interface:
- standardized audit events for logging and dispute resolution

-----------------------------
Core - Server (core-server)
-----------------------------
1) Dimension claim policy service:
- Configurable allow/deny list by dimension key
- Defaults: Overworld claimable; Nether/End unclaimable; Ad Astra Moon/Mars claimable
- Provide API: isClaimableDimension(dimensionKey)

2) Dynmap integration bridge (server-side only; optional):
- Use compileOnly dependency; enable only if dynmap present at runtime
- Create a MarkerSet layer for nation claims
- Listen to claim-change events emitted via Core API
- Render claimed territories as area markers
- Support dimensionKey -> dynmap world name mapping via config
- Performance: do NOT default to one marker per chunk long-term; implement batching and provide a roadmap for polygonization

3) Server feedback and logging:
- Emit deny feedback to client (actionbar/toast/chat configurable)
- Implement message cooldown to prevent spam
- Implement audit logging with enough context: actor, action, reason, position, dimension, involved nation identifiers (if provided by guards)

-----------------------------
Core - Client (core-client)
-----------------------------
1) Shared GUI framework:
- GuiRegistry / ScreenRegistry
- ScreenId system
- Shared Theme/Style tokens (colors, spacing, fonts, icons, sounds)
- Common widgets: lists, tabs, buttons, toast/actionbar feedback, basic overlays

2) Generic GraphView widget (IMPORTANT):
- GraphView is domain-agnostic and reusable
- Supports fixed-position nodes (x,y), edges, zoom, pan, drag
- Hover and click callbacks
- Core-client does NOT implement TechGraphScreen; Tech-client builds that screen using GraphView

3) Deny feedback rendering:
- Render server deny feedback via actionbar/toast/chat
- Respect cooldown and user settings

================================
NATION MOD
================================

Nation manages players, claims, diplomacy (alliances), and war.

Core rules:
- All blocks inside claimed territory are protected from outsiders by default.
- Alliance (“연합”) shares ALL permissions: allied citizens can build/break/use/containers/etc. in each other’s claims.
- War declaration immediately disables ALL permission restrictions between war participants (A, B, C are participants; neutral D is not).
- Neutral nations remain restricted.

Nation responsibilities:
- Nation creation: requires >= 3 players (or equivalent rule)
- Membership and ranks
- Nation treasury (item storage) used for tech costs
- Claiming chunks (dimension-aware using Core dimension policy)
- Alliance system
- War system:
    - war participants as a set of nations (Factorio-style, not simplistic team-only)
    - participant queries: isWarParticipant(n1,n2)
- Provide NationGuard implementation for Core GuardBus (action permission enforcement)
- Emit claim change events and nation style updates to Core for dynmap rendering

Client:
- Use Core GUI framework; register Nation screens/tabs/providers.

================================
TECH MOD
================================

Tech is Factorio-style DAG (not exclusive, not linear). Multiple branches can be researched simultaneously, and branches can merge.

Tech JSON node format:
- stored as datapack-style JSON resources
  Example:

{
"id": "T1-1",
"title": "첫 주조",
"desc": "...",
"parents": ["T1-0"],
"req": {
"items": [
{ "id": "core:pig_iron_ingot", "count": 20 }
]
},
"res": ["permit:tk_smeltery"],
"pos": { "x": 0, "y": 0 },
"category": "metallurgy",
"icon": "tconstruct:smeltery_controller"
}

Rules:
- Tech graph is a DAG: must validate no cycles.
- Unlock requirements consume real resources (items; extensible to fluids/energy/time later).
- Unlock results are PERMITS (strings) that gate crafting/usage/machine output.
- Tech progress stored per nation.
- Special metallurgy rule:
    - Without Tinkers Smeltery permit, “proper casting” is not allowed.
    - Vanilla furnace smelting iron inputs yields Pig Iron (weak durability) instead of standard iron.
    - This behavior must be enforced via TechGuard (and/or recipe/result overriding) consistent with server authority.

Tech responsibilities:
- Load and validate tech JSON
- Track unlocked nodes and permits per nation
- Integrate with Nation treasury to consume costs
- Provide TechGuard for Core GuardBus to enforce permits on:
    - crafting/smelting
    - item use
    - machine outputs
    - relevant block interactions
      Client:
- Implement TechGraphScreen using Core GraphView widget
- Convert TechSnapshot to GraphModel with fixed positions
- Provide UX: node state, cost, missing resources, unlock action, filters, search

================================
ORE MOD
================================

Ore mod controls world generation only.

Rules:
- Vanilla ore distribution is replaced with vein-based generation (ores appear only in veins).
- Add multiple rare ores not present in vanilla.
- Nether/End are unclaimable, but ore generation may still occur there as configured.
- Ore mod contains no nation logic and no tech logic.

Compatibility requirement: “Ore Dictionary” equivalent (IMPORTANT)
- All ores and processed forms MUST be registered into the standard Forge tag system (legacy OreDictionary equivalent) for compatibility.
- Provide tag JSONs under:
    - data/forge/tags/blocks/...
    - data/forge/tags/items/...
- Use standard conventions:
    - #forge:ores/<name>, #forge:ores
    - #forge:raw_materials/<name>, #forge:raw_materials
    - #forge:ingots/<name>, #forge:ingots (if ingots exist)
    - #forge:dusts/<name>, #forge:dusts (if dusts exist)
    - #forge:gems/<name>, #forge:gems (if gems exist)

================================
DYNMAP INTEGRATION (REQUIRED TASKS)
================================

Dynmap is optional at runtime but must be supported.

Requirements:
- Add compileOnly dependency on dynmap API
- Detect dynmap presence at runtime and enable/disable integration
- Maintain marker set for nation claims
- Update markers on claim/unclaim and nation style changes
- Provide config mapping for dimension to dynmap world names
- Avoid performance pitfalls; batch updates and plan polygonization

================================
DELIVERABLE REQUIREMENTS FOR TASK LIST
================================

Generate a structured task breakdown with:
- Clear hierarchy and ordering
- Dependencies between tasks
- Separation by module and by server/client where applicable
- Build system tasks (Gradle, run configs, packaging) included
- Testing tasks included (unit tests where possible, integration tests, dev server runs)
- Data-pack JSON loading tasks included (tech nodes, tags)
- Do NOT write code.
- Produce only the task list.

The task list should start from repository bootstrapping and end with an end-to-end playable prototype:
- Create nations, claim chunks in allowed dimensions, dynmap shows claims
- Alliance permissions fully shared
- War participants bypass all restrictions between them only
- Tech graph loads from JSON, unlock consumes resources, permits gate smeltery/casting behavior
- Ore veins generate and ores are tagged correctly
