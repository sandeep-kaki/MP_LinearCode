# AutoPulse — Day 10 to Day 12 Plan
## Context for Personal Laptop Claude Session

> Read this fully before responding. This is a continuation context.
> Days 1–9 are complete. Pick up from Day 10.

---

## Who Is Sandeep

- QEA trainee at Cognizant → targeting SDET at product company by Nov 2026
- Non-engineering graduate, self-directing aggressively
- Personal laptop only for AutoPulse development going forward
- GitHub: `https://github.com/sandeep-kaki/AutoPulse`
- App under test: `https://automationexercise.com`

---

## Your Role As Claude

- Direct, no fluff
- WHY before HOW — always
- Real-world analogies for every concept
- Complete working code — never partial snippets
- Deep explanation after every class
- Two-part days — Part 1 (concept + first class), Part 2 (remaining + integration)
- Push back honestly when needed
- Celebrate wins — this journey matters

---

## Project State Entering Day 10

**What's built and working:**
- Selenium UI tests with full Page Object Model
- REST Assured API tests (UserApiTest)
- GitHub Actions CI/CD (smoke on push, regression nightly)
- ExtentReports dark theme HTML reports
- FailureAnalyser — calls Groq API (Llama 3.3 70B) on test failure
- AutoPulseListener — wires AI into report on failure
- ScreenshotUtil, ExcelReader, data-driven login tests

**AI Provider: Groq (NOT Gemini)**
- Model: `llama-3.3-70b-versatile`
- Endpoint: `https://api.groq.com/openai/v1/chat/completions`
- Auth: `Authorization: Bearer` header
- Free tier: 14,400 req/day
- Config: `ai.enabled=true` on personal laptop
- GitHub Secret: `GROQ_API_KEY`
- ConfigReader checks `System.getenv("GROQ_API_KEY")` first

**config.properties (personal laptop):**
```properties
base.url=https://automationexercise.com
browser=chrome
headless=false
implicit.wait=10
explicit.wait=15
page.load.timeout=60
api.base.url=https://automationexercise.com/api
reports.path=reports/
screenshots.path=reports/screenshots/
ai.enabled=true
ai.api.key=gsk_YOUR_REAL_GROQ_KEY_HERE
ai.model=llama-3.3-70b-versatile
ai.max.tokens=300
```

**Known issues to fix in Day 10:**
1. UserApiTest.java — `@BeforeClass` calls `ExtentReportManager.initReport()`
   which wipes all UI test results. Report shows only 4 API tests.
2. .gitignore — missing `reports/` so generated HTML is committed.
   Repo shows 91% HTML which looks wrong.
3. README.md — outdated. Says "Claude API (coming soon)". Wrong.
4. FailureAnalyser.java — AI works but response is plain text.
   Needs CAUSE/FIX/PREVENT format + HTML styling.

**Already fixed:**
- SmokeTest.java — correct assertion restored, intentional failure removed.

---

## Day 10 — Clean The Base

### Part 1 — Bug Fixes

**Fix 1: UserApiTest.java**

Remove the `initReport()` call from `@BeforeClass`. The `@BeforeSuite`
in `BaseTest` already initialises the report for the entire suite.
`UserApiTest` calling it again creates a brand new report object,
wiping everything the UI tests wrote before it.

```java
@BeforeClass
public void setUp() {
    userEndpoints = new UserEndpoints();
    // REMOVED: ExtentReportManager.initReport();
    // WHY: BaseTest @BeforeSuite handles report init for entire suite.
    // Calling it here wipes all UI test results already written.
    System.out.println("🔌 API Test Suite Starting...");
    System.out.println("📧 Test email: " + testEmail);
}
```

**Fix 2: .gitignore**

Add these lines to `.gitignore`:
```
reports/
reports/screenshots/
failures.json
```

Then remove the already-committed reports folder from git tracking:
```bash
git rm -r --cached reports/
git add .gitignore
git commit -m "Fix: remove reports from tracking, update gitignore"
```

`git rm --cached` removes from git tracking WITHOUT deleting local files.
The `--cached` flag is the key — without it, git would delete the folder too.

**Fix 3: README.md**

Update the tech stack table — replace the AI row:
```markdown
| AI Analysis | Groq API — Llama 3.3 70B | llama-3.3-70b-versatile |
```

Update the description paragraph to mention Groq and the Self-Healing Agent.
Remove "coming soon" references entirely.

---

### Part 2 — AI Response Formatting

Right now FailureAnalyser returns plain text. The goal: structured,
colour-coded HTML inside the ExtentReport expandable block.

**Why format in FailureAnalyser and not ExtentReportManager?**
FailureAnalyser owns the AI response — it knows the structure.
ExtentReportManager just renders whatever string it receives.
Each class does its own job. Separation of concerns.

**Step 1 — Change buildPrompt() to request labeled format:**

```java
private String buildPrompt(String testName,
                           String errorMessage,
                           String stackTrace,
                           String pageUrl) {
    return String.format(
            "Selenium test failed.\n"
            + "Test: %s\n"
            + "Error: %s\n"
            + "Stack: %s\n\n"
            + "Reply in EXACTLY this format, no other text:\n"
            + "CAUSE: [one sentence root cause]\n"
            + "FIX: [one sentence fix]\n"
            + "PREVENT: [one sentence prevention]",
            testName,
            errorMessage,
            truncateStackTrace(stackTrace, 5)
    );
}
```

**Step 2 — Add formatAsHtml() method:**

```java
/**
 * formatAsHtml() — Converts labeled AI response to styled HTML.
 *
 * Parses CAUSE/FIX/PREVENT labels and wraps each in colour-coded
 * HTML that renders in ExtentReport's dark theme.
 *
 * WHY FORMAT HERE?
 * FailureAnalyser owns the AI response structure.
 * ExtentReportManager just renders what it receives.
 * Separation of concerns — each class does one job.
 */
private String formatAsHtml(String rawAnalysis) {
    try {
        String cause   = extractLabel(rawAnalysis, "CAUSE:");
        String fix     = extractLabel(rawAnalysis, "FIX:");
        String prevent = extractLabel(rawAnalysis, "PREVENT:");

        return "<div style='font-family:Arial,sans-serif;"
                + "padding:10px;line-height:1.8;'>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#ff6b6b;font-weight:bold;'>"
                + "🔍 Root Cause:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>"
                + cause + "</span></p>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#ffd93d;font-weight:bold;'>"
                + "🔧 Fix:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>"
                + fix + "</span></p>"

                + "<p style='margin:6px 0;'>"
                + "<span style='color:#6bcb77;font-weight:bold;'>"
                + "🛡️ Prevention:&nbsp;</span>"
                + "<span style='color:#e0e0e0;'>"
                + prevent + "</span></p>"

                + "</div>";

    } catch (Exception e) {
        // Fallback — never crash because of formatting failure
        // Framework always survives AI layer problems
        return "<p style='color:#e0e0e0;'>"
                + rawAnalysis.replace("\n", "<br>") + "</p>";
    }
}
```

**Step 3 — Add extractLabel() method:**

```java
/**
 * extractLabel() — Pulls value after a label in AI response.
 *
 * Example: "CAUSE: test failed because X" → "test failed because X"
 * Reads from after the label until next newline.
 * Returns "Not available" safely if label not found.
 */
private String extractLabel(String text, String label) {
    int start = text.indexOf(label);
    if (start == -1) return "Not available";
    start += label.length();
    int end = text.indexOf("\n", start);
    if (end == -1) end = text.length();
    return text.substring(start, end).trim();
}
```

**Step 4 — Wire into analyse():**

```java
// Change this line in analyse():
return extractAnalysis(responseBody);

// To this:
String rawAnalysis = extractAnalysis(responseBody);
return formatAsHtml(rawAnalysis);
```

**Day 10 closing commit:**
```bash
git add .
git commit -m "Day 10 complete: Groq AI layer, formatted analysis, clean repo"
git push
```

Verify before committing:
- `mvn test` runs with all test classes in report (not just 4)
- Failed test shows CAUSE/FIX/PREVENT colour-coded in report
- reports/ folder no longer tracked in git

---

## Day 11 — Self-Healing Agent

### Part 1 — Concept + Architecture

**Before writing a single line of code, understand this:**

**The difference between an AI call and an AI agent:**

An AI call is like texting a doctor friend one question.
One message in, one answer out. YOUR code controls the flow.
```
Your code → prompt → AI → answer → done
```

An AI agent is an actual doctor's visit. The doctor asks questions,
orders tests, reads results, orders more tests based on what they find,
then diagnoses. The DOCTOR controls the flow — you just give them a goal.
```
Goal + Tools → AI thinks → requests tool → you run it → result back →
AI thinks again → requests another tool OR gives final verdict
```

The critical insight: **the model never runs your code.**
When the AI "calls a function", it emits a structured message saying
"I want you to run getPageSource". YOUR Java code reads that, runs the
real method, and feeds the result back. Then the AI continues reasoning
with that new information. This is the entire trick behind every AI agent
on earth — from this to the most advanced autonomous coding agents.

**What the Self-Healing Agent does:**

When a test fails with NoSuchElementException:
1. Agent wakes up with goal: "Is this stale locator or real bug?"
2. Agent calls getBrokenLocator() → gets the failed locator
3. Agent calls getPageSource() → gets live DOM
4. Agent reasons: "Old locator missing. Found similar element at..."
5. Agent calls validateLocator("//button[@data-qa='login-button']") → confirms
6. Returns: `HEALED: Use //button[@data-qa='login-button'] | Fix LoginPage.java`

OR if element truly gone:
6. Returns: `REAL_BUG: Element completely absent | [reasoning]`

**Why this is elite:**
Healenium and TestProject charge enterprise money for self-healing locators.
You're building the agent version for free. One full interview minute on its own.
No SDET candidate at 0–1 year experience has built this.

**The agent loop in pseudocode:**
```java
List<Message> messages = buildInitialContext(testName, error, stackTrace);

for (int step = 0; step < MAX_STEPS; step++) {
    Response response = callGroqWithTools(messages, TOOLS);

    if (response.hasToolCall()) {
        String toolName = response.getToolCallName();
        String args     = response.getToolCallArgs();
        String result   = dispatch(toolName, args, driver); // YOU run it
        messages.add(assistantMessage(response));
        messages.add(toolResultMessage(response.getToolCallId(), result));
        // loop continues — AI reasons with new information

    } else {
        return response.getText(); // final verdict
    }
}
return "Agent exceeded step limit — escalating to human review";
```

MAX_STEPS = 6. Every agent needs a leash. This is a real engineering
principle you can say in interviews.

**Three tools the agent can use:**

```
Tool 1: getBrokenLocator()
→ Returns the By locator that failed as a string
→ Agent knows exactly what we were trying to find

Tool 2: getPageSource()
→ Returns full HTML of current live page
→ Agent can see what's actually in the DOM right now

Tool 3: validateLocator(String locator)
→ Your Java code tries the locator against the live page
→ Returns "FOUND: 1 element | tag=button | text=Login"
    OR "NOT_FOUND: No elements matched"
→ This is the agent's "test my hypothesis" tool
→ Makes this a real loop, not just a guess
```

**How Groq function calling works (OpenAI format):**

Request includes a `tools` array:
```json
{
  "model": "llama-3.3-70b-versatile",
  "messages": [...],
  "tools": [
    {
      "type": "function",
      "function": {
        "name": "validateLocator",
        "description": "Tests if a locator finds an element on the current page",
        "parameters": {
          "type": "object",
          "properties": {
            "locator": {
              "type": "string",
              "description": "XPath or CSS selector to test"
            }
          },
          "required": ["locator"]
        }
      }
    }
  ],
  "tool_choice": "auto"
}
```

When AI wants to call a tool, response contains:
```json
{
  "choices": [{
    "message": {
      "role": "assistant",
      "tool_calls": [{
        "id": "call_abc123",
        "type": "function",
        "function": {
          "name": "validateLocator",
          "arguments": "{\"locator\": \"//button[@data-qa='login-button']\"}"
        }
      }]
    },
    "finish_reason": "tool_calls"
  }]
}
```

When AI gives final answer, response contains:
```json
{
  "choices": [{
    "message": {
      "role": "assistant",
      "content": "HEALED: Use //button[@data-qa='login-button']..."
    },
    "finish_reason": "stop"
  }]
}
```

**New file: SelfHealingAgent.java** in `src/main/java/com/autopulse/ai/`

Part 1 ends with: full SelfHealingAgent.java skeleton with agent loop,
tool definitions, and dispatch method. BasePage ThreadLocal<By> change.

---

### Part 2 — Integration + Wiring

**BasePage.java change — track last attempted locator:**

```java
// Add to BasePage:
private static ThreadLocal<By> lastAttemptedLocator = new ThreadLocal<>();

// WHY ThreadLocal?
// Same reason as DriverManager — each parallel test thread has its
// own last-attempted locator. No cross-thread contamination.

// WHY in BasePage and not PageObjects?
// Tracking interaction attempts is a HOW concern.
// BasePage owns HOW. Page Objects own WHAT.
// Every click/type/etc already goes through BasePage methods — 
// perfect place to record without touching any page object.

// Add this line at the START of click(), jsClick(), type():
lastAttemptedLocator.set(locator);

// Getter for agent to use:
public static By getLastAttemptedLocator() {
    return lastAttemptedLocator.get();
}
```

**AutoPulseListener.java change — detect and route locator failures:**

```java
// In onTestFailure() — add locator failure detection:
if (isLocatorFailure(throwable) && driver != null) {
    SelfHealingAgent agent = new SelfHealingAgent(driver);
    String verdict = agent.investigate(testName, errorMessage, stackTrace);
    ExtentReportManager.attachHealingVerdict(verdict);
}

// Helper method:
private boolean isLocatorFailure(Throwable t) {
    if (t == null) return false;
    String msg = t.getClass().getSimpleName();
    return msg.contains("NoSuchElement")
        || msg.contains("StaleElement")
        || (msg.contains("Timeout") && t.getMessage() != null
            && t.getMessage().contains("element"));
}
```

**ExtentReportManager.java change — render verdict:**

```java
public static void attachHealingVerdict(String verdict) {
    boolean healed = verdict.startsWith("HEALED");
    String colour  = healed ? "#6bcb77" : "#ff6b6b";
    String icon    = healed ? "✅ Self-Healed" : "🐛 Real Bug";

    getTest().info(
        "<details><summary>" + icon
        + " — click to expand</summary><br>"
        + "<div style='color:" + colour
        + ";font-weight:bold;padding:8px;border-left:3px solid "
        + colour + ";margin-top:6px;'>"
        + verdict.replace("\n", "<br>")
        + "</div></details>"
    );
}
```

**Day 11 closing commit:**
```bash
git add .
git commit -m "Day 11: Self-Healing Agent with Groq function calling"
git push
```

---

## Day 12 — Final Polish + Interview Prep

### Part 1 — Clean Regression Run + README

**Full regression run:**
```bash
mvn test
```

Every test must pass. Report must show all test classes. AI analysis
must show formatted CAUSE/FIX/PREVENT for any failed test.
Self-Healing Agent verdict must appear for locator failures.

**README.md final version must include:**
- Updated tech stack table with Groq
- Self-Healing Agent description in the features section
- CI badge (already there)
- How to run section
- Two-environment note (ai.enabled flag)

**Final commit:**
```bash
git add .
git commit -m "AutoPulse v1.0 — production ready"
git push
```

---

### Part 2 — Interview Prep

Mock interview session. Claude plays interviewer.
Sandeep answers each question as if in a real interview.
Claude gives feedback on what was strong, what to improve.

**Top questions to prepare:**

1. "Tell me about your AutoPulse project"
2. "Why Page Object Model?"
3. "How does your CI/CD pipeline work?"
4. "Why do you have API tests AND UI tests?"
5. "What is an AI agent vs an AI call?"
6. "How does your Self-Healing Agent work?"
7. "Why Groq over Gemini or OpenAI?"
8. "What was the hardest bug you solved?"
9. "How do you handle parallel test execution?"
10. "What is ThreadLocal and why did you use it?"
11. "What is the Singleton pattern and where did you use it?"
12. "How do you handle test data?"
13. "What happens when the AI service is down?"
14. "How does your framework handle flaky tests?"
15. "What would you add if you had more time?"

**Key answers to lock in:**

"Tell me about your project" (2 minute answer):
AutoPulse is a production-grade multi-layer test automation framework.
It tests automationexercise.com across UI and API layers — Selenium with
Page Object Model for UI, REST Assured for API. GitHub Actions runs smoke
tests on every push and full regression nightly. The differentiator is the
AI layer: when any test fails, the framework captures a screenshot, sends
the failure context to Groq's Llama 3.3 70B, and embeds a formatted root
cause analysis in the ExtentReport. I also built a Self-Healing Agent that
uses Groq's function calling — when a test fails on a locator error, the
agent investigates the live DOM, validates candidate locators, and determines
whether the failure is a stale locator it can heal or a real application bug.

"What is an AI agent vs an AI call?":
An AI call is one shot in, one shot out — your code controls the flow.
An agent is different. You give the model a goal and a set of tools —
functions it can request to have run. The model decides what to investigate
next. My Self-Healing Agent has three tools: fetch page source, get the
broken locator, validate a candidate locator. The agent loops — it observes,
reasons, acts — until it reaches a verdict. The key insight is that the model
never runs your Java code. It emits a structured function call request, my
code runs the actual method and feeds the result back. This is how every
AI agent works, from simple to advanced.

"Why Groq over Gemini?":
I evaluated providers. Groq runs on custom LPU hardware — fastest inference
available anywhere. Llama 3.3 70B supports function calling which I needed
for the agent loop. And it's completely free — 14,400 requests per day,
no credit card. I also had to solve a network constraint — Gemini's API key
format changed and affected my account, so I made an engineering decision
to switch providers rather than block on an infrastructure issue. That's
the kind of problem-solving that happens in real engineering teams.

"What was the hardest debugging session?":
The AI layer required significant debugging across two separate problems.
First, Google AI Studio started generating AQ. format credentials instead of
standard API keys — a restriction triggered after an accidental key exposure
on GitHub. I diagnosed this by isolating each variable with curl tests,
reading Google's specific error codes precisely (ACCESS_TOKEN_TYPE_UNSUPPORTED
vs API_KEY_SERVICE_BLOCKED are completely different problems). Eventually
switched to Groq. Second, the corporate network's SSL inspection agent was
intercepting HTTPS connections and causing PKIX failures in Java. Fixed by
pointing Java to the Windows certificate store. Both problems required
systematic diagnosis, not guessing.

---

## Key Principles (Never Forget)

1. Page Objects own WHAT. BasePage owns HOW.
2. Single Source of Truth — config in one place
3. Separation of Concerns — every class one job
4. Fallbacks never crash the framework
5. alwaysRun=true on all TestNG lifecycle methods
6. Never put secrets in URLs — use headers
7. Every agent needs a leash — MAX_STEPS prevents infinite loops
8. Feature flags per environment — ai.enabled

---

## How To Start Each Day

When Sandeep says "Day 10 Part 1", "Day 11 Part 1", etc:
→ Give that exact content from this document
→ Full explanation, WHY before HOW, real analogies
→ Complete working code, never partial
→ Deep explanation after every class written

When Sandeep says "next" → move to next part.
When Sandeep says "Day X Part Y" → jump to that content.

---

*Personal laptop. Groq working. SmokeTest already fixed.
Day 10 remaining tasks: UserApiTest bug, gitignore, README, AI formatting.
Then Day 11 Self-Healing Agent. Then Day 12 interview prep.*
