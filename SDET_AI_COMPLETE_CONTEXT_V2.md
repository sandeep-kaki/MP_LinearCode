# SDET.ai — Complete Project Context File (Version 2.0)
### For Claude: Read this entire file before responding to anything. This is not a summary. This is the complete truth of who this person is, what they are building, why, and exactly how to interact with them. Do not skip any section. Do not skim. Read everything.

---

## SECTION 1 — WHO YOU ARE TALKING TO

### Personal Profile
- **Company:** Cognizant Technology Solutions
- **Role:** QEA (Quality Engineering Associate)
- **Joined:** January 2025
- **Current situation:** Allocated to a Life Sciences project, waiting for Knowledge Transfer (KT). Using this waiting period to undergo AI training internally.
- **Location:** Chennai, Tamil Nadu, India

### Technical Background — What He Already Knows Well
- **Core Java** — solid understanding
- **Selenium with Java** — strong, hands-on experience
- **Manual Testing** — experienced, understands the full QA lifecycle
- **SQL** — basic to intermediate
- **Git** — basic usage
- **TestNG** — knows annotations, structure, test execution
- **Maven** — knows how to use it for Selenium projects
- **Page Object Model (POM)** — understands and has used it

### Technical Background — Currently Learning
- **Python** — beginner level. Learns by building, not by studying theory separately.
- **Machine Learning** — covered in Cognizant training
- **Neural Networks** — covered in Cognizant training
- **NLP** — covered in Cognizant training
- **Transformers** — covered in Cognizant training

### Upcoming Training Topics (Not Yet Started)
- Agentic AI
- RAG (Retrieval Augmented Generation)
- Context Engineering
- Prompt Engineering

### Python Approach — CRITICAL
He wants to **learn Python directly through building this project**. Do NOT suggest he take a Python course separately. Every Python concept is introduced on the exact day he needs it to build the next piece. Theory and building always happen together.

---

## SECTION 2 — HIS MINDSET AND LEARNING STYLE

### How He Thinks
- He thinks **like an SDET first, AI second**. The project must position him as a Test Engineer who has mastered Agentic AI — not as an AI developer who also knows testing.
- He asks sharp, honest questions. He pushes back when something doesn't feel right. Respect that instinct always.
- He thinks about the long game — career trajectory, how skills compound, what differentiates him.

### How He Learns
- **Discussion style** — conversation, not lectures. Ask him questions. Let him reason. Build on his reasoning.
- **Stage by stage** — never overwhelm with everything at once. One concept, one task, one day.
- **Building = understanding** — if he cannot explain what he built, he has not learned it yet.
- **Never copy-paste without understanding** — if he pastes code he does not understand, stop and explain before moving on. Hard rule.
- **Honest feedback** — tell him the truth always. He does not want to be coddled.

### His Career Goal
**SDET role at a product company within approximately 1 year of joining Cognizant (January 2025).**

His positioning statement — memorise this:
> *"I think like an SDET, and I've enhanced that thinking with AI."*

This is the exact truth of who he is becoming. Every conversation reinforces this positioning.

### Why Product Company
- Faster career growth
- End-to-end ownership
- Deeper skill development
- Less project-hopping than consulting

---

## SECTION 3 — THE PROJECT — SDET.ai

### Project Name
**SDET.ai** — *An intelligent test maintenance agent for Selenium frameworks.*

### One-Line Description
A Python AI agent that sits alongside an existing Java Selenium framework, watches for test failures, classifies what went wrong using AI reasoning, takes the right action for that specific failure type, heals the framework automatically, predicts what will break next, and gets smarter over time through RAG.

### The Core Problem It Solves
**Test maintenance is a full-time job.** Every UI change breaks multiple Selenium tests. SDETs spend more time fixing broken tests than building new ones. This happens every sprint, in every product company, without exception.

### The 5 Failure Types — The Heart of the Project

| Type | Name | What It Means | What the Agent Does |
|------|------|---------------|---------------------|
| 1 | LOCATOR | Element exists but selector broke | Re-inspect DOM, find correct locator, patch Java file |
| 2 | TIMING | Element exists but page wasn't ready | Replace bad wait with intelligent WebDriverWait |
| 3 | DATA | Test data is stale, wrong, or expired | Refresh/flag test data, externalise from hardcode |
| 4 | GENUINE_BUG | Feature actually broke | Do NOT touch test. Generate structured bug report. |
| 5 | ENVIRONMENT | Staging down, network issue, server error | Retry once with backoff. Alert if persistent. |

**Type 4 is the most important.** Knowing when NOT to fix is the sign of senior-level thinking.

### Tech Stack
- **Java** — owns the Selenium test framework. TestNG, Maven, POM, test execution.
- **Python** — owns the AI agent. LLM calls, classification, DOM inspection, code patching, RAG.
- **Communication** — through files and subprocess. Python reads JSON that Java writes. Python triggers Maven via subprocess.

### Demo Target
- **Primary:** SauceDemo (saucedemo.com)
- **Advanced:** OrangeHRM (demo.orangehrm.com)

### Confidence Scoring
- Above 0.85 — agent acts automatically
- 0.60 to 0.85 — agent acts but flags for review
- Below 0.60 — agent does NOT act. Escalates to human. Writes to escalation_queue.json.

---

## SECTION 4 — PROJECT ARCHITECTURE — 5 COMPONENTS

### Component 1 — The Watcher
Collects complete failure context when a test fails.
- StackTraceParser — extracts exception type, failing line, root cause
- ScreenshotAnalyser — sends screenshot to Claude, gets visual context
- DOMCollector — visits live page, extracts condensed interactive elements
- TestCodeReader — extracts the exact failing Java test method
- FailureHistoryTracker — retrieves past failures for this test
- Watcher — orchestrates all 5, returns complete AgentState

### Component 2 — The Classifier
AI brain. Reads AgentState, classifies failure type with confidence score.
- Uses Claude to reason across all signals simultaneously
- Returns: failure_type, confidence (0-1), reasoning, recommended_action, needs_human_review
- Enhanced with RAG — retrieves similar past failures for few-shot reasoning

### Component 3 — The Fixer
Five specialist handlers routed by classification.
- LocatorHealer — patches Java POM file with correct locator
- TimingFixer — replaces bad waits with WebDriverWait
- DataRefresher — externalises hardcoded test data
- BugReporter — generates structured defect report, does NOT touch test code
- EnvironmentHandler — retries with backoff, alerts if persistent
- RetryLoop (ReAct pattern) — re-runs test after fix, loops back if still failing, max 3 attempts

### Component 4 — The Predictor
Proactively scans healthy tests for fragility.
- CodeAnalyser — detects Thread.sleep, dynamic XPaths, hardcoded data, missing waits
- FragilityScorer — AI scores each test 0-10 with risk factors and recommendations
- PredictionReportGenerator — ranks tests by fragility, priority queue for fixes

### Component 5 — The Memory (RAG)
Stores every agent event. Retrieves past experience to improve decisions.
- MemoryWriter — stores failure + classification + fix + outcome in ChromaDB
- MemoryReader — retrieves 3 most similar past cases, injects into Classifier prompt
- Tech: ChromaDB + sentence-transformers (local, free)

---

## SECTION 5 — FOLDER STRUCTURE

```
sdet-ai/                          ← Root Python AI Agent
├── agents/
│   ├── watcher/
│   │   ├── stack_trace_parser.py
│   │   ├── screenshot_analyser.py
│   │   ├── dom_collector.py
│   │   ├── test_code_reader.py
│   │   ├── failure_history_tracker.py
│   │   └── watcher.py
│   ├── classifier/
│   │   ├── classifier.py
│   │   ├── classifier_prompt.py
│   │   └── escalation_handler.py
│   ├── fixer/
│   │   ├── base_fixer.py
│   │   ├── fixer_router.py
│   │   ├── locator_healer.py
│   │   ├── timing_fixer.py
│   │   ├── data_refresher.py
│   │   ├── bug_reporter.py
│   │   ├── environment_handler.py
│   │   ├── execution_engine.py
│   │   └── retry_loop.py
│   ├── predictor/
│   │   ├── code_analyser.py
│   │   ├── fragility_scorer.py
│   │   └── prediction_report_generator.py
│   └── memory/
│       ├── memory_writer.py
│       └── memory_reader.py
├── core/
│   ├── llm_client.py
│   ├── pipeline.py
│   └── state.py
├── utils/
│   ├── file_writer.py
│   └── dom_inspector.py
├── output/
│   ├── failure-reports/
│   ├── screenshots/
│   ├── reports/
│   └── escalation_queue.json
├── app.py
├── main.py
└── requirements.txt

sdet-ai-framework/                ← Java Selenium Framework
├── src/
│   ├── main/java/pages/
│   │   ├── BasePage.java
│   │   ├── LoginPage.java
│   │   └── InventoryPage.java
│   └── test/java/
│       ├── base/BaseTest.java
│       ├── listeners/FailureCaptureListener.java
│       └── tests/LoginTests.java
└── pom.xml
```

---

## SECTION 6 — COMPLETE 75-DAY PLAN — EVERY DAY IN FULL DETAIL

### HOW TO READ EACH DAY
Every day has exactly 4 parts:
- **Concept** — what to learn and understand today
- **Why it matters** — how this connects to SDET.ai specifically
- **Task** — exactly what to build
- **Exercise** — a small challenge to test understanding (do this after the task)
- **Expected output** — what you should have at the end of the day

---

## PHASE 1 — FOUNDATION (Days 1–14)
**Goal:** Learn Python through building real utilities. Set up the Java framework. Connect both worlds.
**Milestone:** Day 14 — Java writes failure JSON → Python reads it. Two worlds connected.

---

### DAY 1 — Environment Setup + Verification

**Concept:** Why SDET.ai uses Python and Java together. Python owns the AI brain. Java owns the test execution. They talk through files. Neither language needs to know the other exists directly.

**Why it matters:** A broken environment on Day 1 wastes hours on Day 5. Verify everything now so you never debug a setup issue mid-build.

**Task:** Run all 7 checks below in order. Do not skip any.

```
Check 1 — Java
java -version        (need Java 8 or above)
javac -version       (both must return a version)

Check 2 — Maven
mvn -version         (must return Maven version + Java it is using)

Check 3 — Python
python --version     (need Python 3.8 or above)
pip --version        (must return a version)

Check 4 — Git
git --version
git config --global user.name
git config --global user.email
(both name and email must return your values)

Check 5 — VS Code Extensions
Open VS Code. Confirm installed:
- Extension Pack for Java (Microsoft)
- Python (Microsoft)
- GitLens (GitKraken)

Check 6 — Selenium + Chrome
Create check_selenium.py and run it:

from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager

driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()))
driver.get("https://saucedemo.com")
print(driver.title)
driver.quit()

Expected output: "Swag Labs" printed. Chrome opens and closes.

Check 7 — Anthropic
pip install anthropic
Then run:
import anthropic
print("Anthropic ready")
```

**GitHub setup:**
```
mkdir sdet-ai
cd sdet-ai
git init
git remote add origin YOUR_GITHUB_URL
touch README.md
git add .
git commit -m "Day 1: Project initialised"
git push -u origin main
```

**Exercise:** After all 7 checks pass — write a single Python script called `health_check.py` that prints a summary of what is installed. Example output:
```
Java: installed
Maven: installed
Python: installed
Selenium: installed
Anthropic: installed
All systems ready for SDET.ai
```

**Expected output:** GitHub repo live. All 7 checks green. health_check.py working.

**Signal to proceed to Day 2:** Say "All 7 checks passed. Repo created."

---

### DAY 2 — Python Basics: Variables, Functions, Dictionaries

**Concept:** Variables, strings, integers, booleans, lists, dictionaries, functions, f-strings, print statements. In Python you do not declare types. Indentation is how Python knows what belongs inside a function or block — there are no curly braces.

**Why it matters:** On Day 12 you will parse failure JSON from Java. The data will come as dictionaries. Today you build the muscle memory for handling dictionaries before the project needs it.

**Task:** Create `failure_reader.py`
```python
# A test just failed. This is what the failure looks like as a dictionary.
failure = {
    "test_name": "testValidLogin",
    "error_type": "NoSuchElementException",
    "error_message": "Unable to locate element: #login-button",
    "timestamp": "2025-01-15 10:30:00",
    "screenshot": "screenshots/testValidLogin_fail.png"
}

def print_failure_summary(failure_data):
    print(f"Test Failed: {failure_data['test_name']}")
    print(f"Error: {failure_data['error_type']}")
    print(f"Message: {failure_data['error_message']}")
    print(f"Time: {failure_data['timestamp']}")
    print(f"Screenshot: {failure_data['screenshot']}")

print_failure_summary(failure)
```

**Exercise 1:** Add a second dictionary for a different failure (timing failure — TimeoutException). Call the same function for both failures.

**Exercise 2:** Write a function called `is_critical_failure(failure_data)` that returns True if the error_type is "NoSuchElementException" or "TimeoutException", and False otherwise. Print "CRITICAL" or "NON-CRITICAL" for each failure.

**Expected output:** failure_reader.py running, printing summaries for 2 failures, correctly labelling them.

---

### DAY 3 — Python: File I/O and JSON

**Concept:** Reading from files, writing to files, opening files safely with `with open()`. JSON module — `json.loads()` converts a JSON string to a Python dictionary, `json.dumps()` converts a dictionary back to JSON string, `json.load()` reads JSON directly from a file, `json.dump()` writes JSON directly to a file.

**Why it matters:** The Java TestNG listener will write failure details as JSON files to a folder. Your Python Watcher will read those JSON files. This is the exact bridge between the two languages. Today you practise that bridge manually.

**Task:** Create two files:

First, create `sample_failure.txt` manually with this content:
```
Test: testValidLogin
Error: NoSuchElementException
Message: Unable to locate element: #login-button
Line: LoginPage.java:45
Timestamp: 2025-01-15 10:30:00
```

Then create `log_parser.py`:
```python
import json

# Step 1: Read the raw text file
with open("sample_failure.txt", "r") as f:
    raw_content = f.read()

print("Raw content read successfully")
print(raw_content)

# Step 2: Parse into a structured dictionary
def parse_failure_text(content):
    lines = content.strip().split("\n")
    failure = {}
    for line in lines:
        if ":" in line:
            key, value = line.split(":", 1)
            failure[key.strip().lower().replace(" ", "_")] = value.strip()
    return failure

failure_dict = parse_failure_text(raw_content)
print("\nParsed dictionary:")
print(failure_dict)

# Step 3: Save as JSON file
with open("output/failure_001.json", "w") as f:
    json.dump(failure_dict, f, indent=2)

print("\nSaved as JSON successfully")

# Step 4: Read it back to verify
with open("output/failure_001.json", "r") as f:
    loaded = json.load(f)

print("\nLoaded back from JSON:")
print(loaded["test"])
```

**Exercise 1:** Create a second sample_failure_2.txt for a different test (use TimeoutException). Parse it and save as failure_002.json.

**Exercise 2:** Write a function `load_all_failures(folder_path)` that reads ALL json files in a folder and returns a list of failure dictionaries.

**Expected output:** Two JSON files in output/ folder. Both loaded back correctly.

---

### DAY 4 — Python: Loops, Conditionals, Error Handling

**Concept:** `for` loops, `while` loops, `if/elif/else`, `try/except/finally`. Why error handling matters in agents — if one failure file is corrupted, the agent should skip it and continue, not crash entirely.

**Why it matters:** The Watcher will process multiple failure files at once. Some files may be malformed. Without try/except the whole agent crashes on one bad file. Today you build the habit of defensive coding.

**Task:** Extend log_parser.py — create `batch_parser.py`:
```python
import json
import os

failures = [
    {"test_name": "testValidLogin", "error_type": "NoSuchElementException", "message": "Cannot find #login-btn"},
    {"test_name": "testInvalidPassword", "error_type": "TimeoutException", "message": "Element not clickable"},
    {"test_name": "testEmptyUsername", "error_type": None, "message": ""},  # malformed
    {"test_name": "testLockedUser", "error_type": "AssertionError", "message": "Expected: Success, Got: Error"},
    None  # completely broken entry
]

def classify_error(error_type):
    if error_type == "NoSuchElementException":
        return "LOCATOR"
    elif error_type == "TimeoutException":
        return "TIMING"
    elif error_type == "AssertionError":
        return "GENUINE_BUG"
    else:
        return "UNKNOWN"

processed = []
skipped = 0

for failure in failures:
    try:
        if failure is None:
            raise ValueError("Empty failure entry")
        if not failure.get("error_type"):
            raise ValueError(f"Missing error_type in {failure['test_name']}")
        
        failure["classification"] = classify_error(failure["error_type"])
        processed.append(failure)
        print(f"Processed: {failure['test_name']} → {failure['classification']}")
        
    except Exception as e:
        skipped += 1
        print(f"Skipped malformed entry: {e}")

print(f"\nTotal processed: {len(processed)}")
print(f"Total skipped: {skipped}")
```

**Exercise 1:** Add a `while` loop that keeps asking the user to enter an error type (input()) and returns the classification until they type "exit".

**Exercise 2:** Add a check — if error_type is "AssertionError" AND the message contains "Expected:", print "LIKELY GENUINE BUG — do not auto-fix". Otherwise print normal classification.

**Expected output:** batch_parser.py running, correctly classifying 3 failures, gracefully skipping 2 malformed ones.

---

### DAY 5 — Python: Classes and Modules

**Concept:** Classes, `__init__`, instance methods, `self`, importing from other files using `import`. Think of Python classes exactly like Java classes — same concept, different syntax. `self` in Python = `this` in Java.

**Why it matters:** Every component of SDET.ai is a class — FailureParser, Watcher, Classifier, LocatorHealer. Today you learn the pattern that every future day builds on.

**Task:** Refactor log_parser.py into a proper class. Create `agents/watcher/failure_parser.py`:
```python
import json
import os

class FailureParser:
    """Parses raw failure data into structured AgentState-ready format."""
    
    def __init__(self, failure_reports_dir: str):
        self.failure_reports_dir = failure_reports_dir
        self.parsed_failures = []
    
    def parse_single(self, file_path: str) -> dict:
        """Parse one JSON failure file into a structured dictionary."""
        with open(file_path, "r") as f:
            data = json.load(f)
        return {
            "test_name": data.get("test_name", "unknown"),
            "error_type": data.get("error_type", "unknown"),
            "error_message": data.get("error_message", ""),
            "stack_trace": data.get("stack_trace", ""),
            "timestamp": data.get("timestamp", "")
        }
    
    def parse_all(self) -> list:
        """Parse all JSON files in the failure reports directory."""
        results = []
        for filename in os.listdir(self.failure_reports_dir):
            if filename.endswith(".json"):
                try:
                    path = os.path.join(self.failure_reports_dir, filename)
                    parsed = self.parse_single(path)
                    results.append(parsed)
                    print(f"Parsed: {filename}")
                except Exception as e:
                    print(f"Skipped {filename}: {e}")
        self.parsed_failures = results
        return results
    
    def get_summary(self) -> str:
        """Return a human-readable summary of all parsed failures."""
        if not self.parsed_failures:
            return "No failures parsed yet."
        summary = f"Total failures: {len(self.parsed_failures)}\n"
        for f in self.parsed_failures:
            summary += f"  - {f['test_name']}: {f['error_type']}\n"
        return summary
```

Then create `main.py` that imports and uses it:
```python
from agents.watcher.failure_parser import FailureParser

parser = FailureParser("output/")
failures = parser.parse_all()
print(parser.get_summary())
```

**Exercise 1:** Add a method `get_by_type(error_type)` that returns only failures matching a specific error type.

**Exercise 2:** Add a method `save_summary(output_path)` that writes the summary string to a text file.

**Expected output:** FailureParser class working as a reusable module. main.py imports and uses it cleanly.

---

### DAY 6 — Python: Calling the Anthropic API

**Concept:** The `anthropic` library. How to create a client. How to send a message with a system prompt and a user prompt. What the response object looks like. How to extract the text from the response.

**Why it matters:** Every single component of SDET.ai calls the Claude API. Today you make your first real AI call with real failure data. This is the moment the project becomes an AI project.

**Task:** Create `core/llm_client.py`:
```python
import anthropic
import os

# The client automatically reads ANTHROPIC_API_KEY from environment
client = anthropic.Anthropic()

def call_llm(system_prompt: str, user_prompt: str, max_tokens: int = 1000) -> str:
    """Send a prompt to Claude and return the text response."""
    message = client.messages.create(
        model="claude-sonnet-4-6",
        max_tokens=max_tokens,
        system=system_prompt,
        messages=[
            {"role": "user", "content": user_prompt}
        ]
    )
    return message.content[0].text
```

Then create `first_ai_call.py`:
```python
from core.llm_client import call_llm

failure_context = """
Test Name: testValidLogin
Error Type: NoSuchElementException
Error Message: Unable to locate element: {'css selector': '#login-button'}
Stack Trace: 
  at LoginPage.clickLoginButton(LoginPage.java:45)
  at LoginTests.testValidLogin(LoginTests.java:23)
"""

system = "You are a senior QA engineer analysing Selenium test failures."
user = f"Analyse this test failure and tell me what likely caused it:\n{failure_context}"

response = call_llm(system, user)
print("Claude's analysis:")
print(response)
```

**Set your API key first:**
```bash
# On Windows (Command Prompt)
set ANTHROPIC_API_KEY=your_key_here

# On Mac/Linux
export ANTHROPIC_API_KEY=your_key_here
```

**Exercise 1:** Change the failure context to a TimeoutException. Run again. Notice how Claude's explanation changes.

**Exercise 2:** Try asking "What type of failure is this — locator, timing, data, genuine bug, or environment?" Observe if Claude classifies it correctly without any guidance.

**Expected output:** Claude responding with a real analysis of your failure. First AI call working.

---

### DAY 7 — Python: Structured Output from LLM

**Concept:** Why LLMs return unpredictable prose by default. How system prompts force structured JSON output. How to parse that JSON reliably. Why structured output is non-negotiable in production agents — you cannot build logic on top of unpredictable text.

**Why it matters:** The Classifier returns a JSON object. The Fixer reads specific keys from that object. If the LLM returns prose instead of JSON, the whole pipeline breaks. Today you learn to make the LLM behave reliably.

**Task:** Create `structured_classifier.py`:
```python
import json
from core.llm_client import call_llm

CLASSIFIER_SYSTEM = """You are an expert Selenium test failure classifier.

When given a test failure, classify it into exactly one of these types:
- LOCATOR: The element selector is wrong or outdated
- TIMING: The element exists but the page was not ready
- DATA: The test data is wrong, stale, or missing
- GENUINE_BUG: The application feature actually broke
- ENVIRONMENT: Infrastructure issue, server down, network problem

You MUST respond with ONLY valid JSON. No explanation before or after. No markdown. No backticks.
Exact format:
{
  "failure_type": "LOCATOR",
  "confidence": 0.92,
  "reasoning": "Your reasoning here in one sentence",
  "recommended_action": "What the agent should do",
  "needs_human_review": false
}"""

def classify_failure(failure_context: str) -> dict:
    """Classify a failure and return structured result."""
    response = call_llm(CLASSIFIER_SYSTEM, failure_context)
    
    # Parse JSON safely
    try:
        result = json.loads(response)
        return result
    except json.JSONDecodeError as e:
        print(f"JSON parse failed: {e}")
        print(f"Raw response was: {response}")
        return {"failure_type": "UNKNOWN", "confidence": 0.0, "needs_human_review": True}

# Test with a locator failure
locator_failure = """
Test: testValidLogin
Error: NoSuchElementException: Unable to locate element: #login-button
Stack trace: LoginPage.clickLoginButton(LoginPage.java:45)
Screenshot shows: Login page loaded correctly, form visible, but button ID appears different
"""

result = classify_failure(locator_failure)
print(f"Type: {result['failure_type']}")
print(f"Confidence: {result['confidence']}")
print(f"Reasoning: {result['reasoning']}")
print(f"Action: {result['recommended_action']}")
print(f"Human review needed: {result['needs_human_review']}")
```

**Exercise 1:** Test with a timing failure (TimeoutException). Verify JSON comes back correctly.

**Exercise 2:** Intentionally break the system prompt by removing the JSON format instruction. Observe what the LLM returns. Then fix it. This teaches you why the format instruction is critical.

**Exercise 3:** Add a validation function `validate_classification(result: dict) -> bool` that checks all required keys exist and confidence is between 0 and 1.

**Expected output:** Reliable JSON from Claude every single time. Validation working.

---

### DAY 8 — Java: Maven Project Setup for SDET.ai Framework

**Concept:** Maven project structure. pom.xml — what it is and how it works. Adding Selenium, TestNG, and WebDriverManager dependencies. Why we separate the Java framework into its own folder from the Python agent.

**Why it matters:** The Java framework is what produces real test failures. Without it, the Python agent has nothing to watch. Today you create the foundation the agent will sit on top of.

**Task:** Create Maven project `sdet-ai-framework`. Use this pom.xml:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.sdetal</groupId>
    <artifactId>sdet-ai-framework</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.18.1</version>
        </dependency>
        <dependency>
            <groupId>org.testng</groupId>
            <artifactId>testng</artifactId>
            <version>7.9.0</version>
        </dependency>
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.7.0</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>2.16.1</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.2.5</version>
                <configuration>
                    <suiteXmlFiles>
                        <suiteXmlFile>testng.xml</suiteXmlFile>
                    </suiteXmlFiles>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

Run: `mvn compile` — must succeed with BUILD SUCCESS.

Create folder structure:
```
src/main/java/com/sdetal/pages/
src/test/java/com/sdetal/base/
src/test/java/com/sdetal/listeners/
src/test/java/com/sdetal/tests/
```

Also create these output folders in the project root:
```
failure-reports/
screenshots/
```

**Exercise:** Add a `TestUtils.java` class in main/java with one static method `getCurrentTimestamp()` that returns a formatted timestamp string. Verify it compiles.

**Expected output:** Maven project compiling cleanly. All folders created.

---

### DAY 9 — Java: BasePage and BaseTest

**Concept:** BasePage pattern — shared Selenium utilities all page objects inherit. BaseTest — WebDriver lifecycle management, setup before test, teardown after test, screenshot capture on failure.

**Why it matters:** Every Page Object will extend BasePage. Every test class will extend BaseTest. This is the exact same OOP pattern you know from Java — just applied to Selenium framework design.

**Task — BasePage.java:**
```java
package com.sdetal.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class BasePage {
    protected WebDriver driver;
    protected WebDriverWait wait;
    
    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }
    
    protected WebElement findElement(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }
    
    protected void click(By locator) {
        findElement(locator).click();
    }
    
    protected void type(By locator, String text) {
        WebElement element = findElement(locator);
        element.clear();
        element.sendKeys(text);
    }
    
    protected String getText(By locator) {
        return findElement(locator).getText();
    }
    
    protected boolean isDisplayed(By locator) {
        try {
            return driver.findElement(locator).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
```

**Task — BaseTest.java:**
```java
package com.sdetal.base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class BaseTest {
    protected WebDriver driver;
    
    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }
    
    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            captureScreenshot(result.getName());
        }
        if (driver != null) {
            driver.quit();
        }
    }
    
    private void captureScreenshot(String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String destPath = "screenshots/" + testName + "_" + timestamp + ".png";
            FileUtils.copyFile(source, new File(destPath));
            System.out.println("Screenshot saved: " + destPath);
        } catch (IOException e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }
}
```

**Exercise:** Add a method `getPageTitle()` to BaseTest that returns the current page title. Add `navigateTo(String url)` that navigates to a URL. Both will be used in tests starting Day 11.

**Expected output:** BasePage.java + BaseTest.java compiling cleanly. No red errors in VS Code.

---

### DAY 10 — Java: SauceDemo Page Objects

**Concept:** POM in practice. One class per page. Locators as `By` constants at the top. All interactions as methods. Why tests should never contain locators — only page objects should.

**Why it matters:** The LocatorHealer (Fixer 1) will patch locators in these exact files. You need clean, well-structured page objects for the healer to modify safely.

**Task — LoginPage.java:**
```java
package com.sdetal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends BasePage {
    
    // Locators — the Healer will patch these when they break
    private static final By USERNAME = By.id("user-name");
    private static final By PASSWORD = By.id("password");
    private static final By LOGIN_BUTTON = By.id("login-button");
    private static final By ERROR_MESSAGE = By.cssSelector("[data-test='error']");
    
    public LoginPage(WebDriver driver) {
        super(driver);
    }
    
    public void navigateToLoginPage() {
        driver.get("https://www.saucedemo.com");
    }
    
    public void enterUsername(String username) {
        type(USERNAME, username);
    }
    
    public void enterPassword(String password) {
        type(PASSWORD, password);
    }
    
    public void clickLoginButton() {
        click(LOGIN_BUTTON);
    }
    
    public void login(String username, String password) {
        navigateToLoginPage();
        enterUsername(username);
        enterPassword(password);
        clickLoginButton();
    }
    
    public String getErrorMessage() {
        return getText(ERROR_MESSAGE);
    }
    
    public boolean isErrorDisplayed() {
        return isDisplayed(ERROR_MESSAGE);
    }
}
```

**Task — InventoryPage.java:**
```java
package com.sdetal.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import java.util.List;

public class InventoryPage extends BasePage {
    
    private static final By INVENTORY_LIST = By.className("inventory_list");
    private static final By PRODUCT_NAMES = By.className("inventory_item_name");
    private static final By CART_BADGE = By.className("shopping_cart_badge");
    private static final By ADD_TO_CART_BUTTONS = By.cssSelector("[data-test^='add-to-cart']");
    
    public InventoryPage(WebDriver driver) {
        super(driver);
    }
    
    public boolean isInventoryPageLoaded() {
        return isDisplayed(INVENTORY_LIST);
    }
    
    public int getProductCount() {
        return driver.findElements(PRODUCT_NAMES).size();
    }
    
    public String getFirstProductName() {
        return getText(PRODUCT_NAMES);
    }
    
    public void addFirstItemToCart() {
        driver.findElements(ADD_TO_CART_BUTTONS).get(0).click();
    }
    
    public String getCartCount() {
        return getText(CART_BADGE);
    }
}
```

**Exercise:** Create `CheckoutPage.java` with at least 3 locators and 2 methods. You choose what to add. This proves you can create a Page Object independently.

**Expected output:** LoginPage.java + InventoryPage.java + CheckoutPage.java all compiling.

---

### DAY 11 — Java: Full TestNG Test Suite for SauceDemo

**Concept:** TestNG annotations — @Test, @BeforeMethod, @AfterMethod. Writing meaningful assertions with Assert class. Good test naming conventions — test names should describe the scenario, not the implementation.

**Why it matters:** These 8 tests will be the ones your agent watches, breaks, classifies, and heals. You need to know exactly what each test does so you can verify the agent is healing them correctly.

**Task — LoginTests.java:**
```java
package com.sdetal.tests;

import com.sdetal.base.BaseTest;
import com.sdetal.pages.InventoryPage;
import com.sdetal.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginTests extends BaseTest {
    
    @Test(description = "Valid credentials should grant access to inventory")
    public void testValidLogin() {
        LoginPage loginPage = new LoginPage(driver);
        InventoryPage inventoryPage = new InventoryPage(driver);
        loginPage.login("standard_user", "secret_sauce");
        Assert.assertTrue(inventoryPage.isInventoryPageLoaded(), 
            "Inventory page should load after valid login");
    }
    
    @Test(description = "Invalid password should show error message")
    public void testInvalidPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "wrong_password");
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "Error message should appear for wrong password");
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username and password do not match"));
    }
    
    @Test(description = "Empty username should show validation error")
    public void testEmptyUsername() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Username is required"));
    }
    
    @Test(description = "Empty password should show validation error")
    public void testEmptyPassword() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("standard_user", "");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Password is required"));
    }
    
    @Test(description = "Locked user should see specific locked-out error")
    public void testLockedUserLogin() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("locked_out_user", "secret_sauce");
        Assert.assertTrue(loginPage.isErrorDisplayed());
        Assert.assertTrue(loginPage.getErrorMessage().contains("Sorry, this user has been locked out"));
    }
    
    @Test(description = "SQL injection attempt should not bypass login")
    public void testSQLInjectionAttempt() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("' OR '1'='1", "' OR '1'='1");
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "SQL injection should not bypass authentication");
    }
    
    @Test(description = "Maximum length input should be handled gracefully")
    public void testMaxLengthInput() {
        LoginPage loginPage = new LoginPage(driver);
        String longInput = "a".repeat(500);
        loginPage.login(longInput, longInput);
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "Max length input should result in error, not crash");
    }
    
    @Test(description = "Special characters in credentials should be handled")
    public void testSpecialCharacterInput() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("test@#$%^&*()", "test!@#$%");
        Assert.assertTrue(loginPage.isErrorDisplayed(), 
            "Special characters should not break the login form");
    }
}
```

Create `testng.xml`:
```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE suite SYSTEM "http://testng.org/testng-1.0.dtd">
<suite name="SDET.ai Test Suite" verbose="1">
    <test name="Login Tests">
        <classes>
            <class name="com.sdetal.tests.LoginTests"/>
        </classes>
    </test>
</suite>
```

Run: `mvn test` — all 8 tests should pass.

**Exercise:** Add a 9th test `testInventoryLoadAfterLogin()` that verifies exactly 6 products appear on the inventory page after successful login. Run the suite. Verify 9/9 pass.

**Expected output:** 9 tests running and passing with mvn test.

---

### DAY 12 — Java: TestNG Listener — The Bridge Builder

**Concept:** ITestListener interface in TestNG. The onTestFailure method — fires automatically every time a test fails. How to capture failure data and write it as JSON. This is the most important Java file in the entire project — it is the bridge between Java and Python.

**Why it matters:** The Python Watcher cannot watch what it cannot see. This listener is what makes failures visible to Python. Without it, the agent is blind.

**Task — FailureCaptureListener.java:**
```java
package com.sdetal.listeners;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class FailureCaptureListener implements ITestListener {
    
    private static final String FAILURE_REPORTS_DIR = "failure-reports/";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    public void onTestFailure(ITestResult result) {
        System.out.println("SDET.ai Listener: Capturing failure for " + result.getName());
        
        Map<String, Object> failureData = new HashMap<>();
        failureData.put("test_name", result.getName());
        failureData.put("test_class", result.getTestClass().getName());
        failureData.put("description", result.getMethod().getDescription());
        
        Throwable throwable = result.getThrowable();
        if (throwable != null) {
            failureData.put("error_type", throwable.getClass().getSimpleName());
            failureData.put("error_message", throwable.getMessage());
            failureData.put("stack_trace", getStackTrace(throwable));
        }
        
        String timestamp = LocalDateTime.now()
            .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        failureData.put("timestamp", timestamp);
        failureData.put("screenshot_path", "screenshots/" + result.getName() + "_" + timestamp + ".png");
        
        // Find the URL the test was on (if WebDriver available via ThreadLocal in future)
        failureData.put("page_url", "https://www.saucedemo.com");
        
        saveFailureReport(result.getName(), timestamp, failureData);
    }
    
    private String getStackTrace(Throwable throwable) {
        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : throwable.getStackTrace()) {
            sb.append(element.toString()).append("\n");
            // Only capture first 10 lines to keep JSON manageable
            if (sb.toString().split("\n").length > 10) break;
        }
        return sb.toString();
    }
    
    private void saveFailureReport(String testName, String timestamp, Map<String, Object> data) {
        try {
            new File(FAILURE_REPORTS_DIR).mkdirs();
            String fileName = FAILURE_REPORTS_DIR + testName + "_" + timestamp + ".json";
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(fileName), data);
            System.out.println("SDET.ai: Failure report saved → " + fileName);
        } catch (Exception e) {
            System.err.println("SDET.ai: Failed to save report: " + e.getMessage());
        }
    }
    
    // Required interface methods — leave empty for now
    @Override public void onTestStart(ITestResult result) {}
    @Override public void onTestSuccess(ITestResult result) {}
    @Override public void onTestSkipped(ITestResult result) {}
}
```

Register listener in testng.xml:
```xml
<suite name="SDET.ai Test Suite" verbose="1">
    <listeners>
        <listener class-name="com.sdetal.listeners.FailureCaptureListener"/>
    </listeners>
    <test name="Login Tests">
        <classes>
            <class name="com.sdetal.tests.LoginTests"/>
        </classes>
    </test>
</suite>
```

Intentionally break one test — change the login button locator to something wrong:
```java
private static final By LOGIN_BUTTON = By.id("WRONG_BUTTON_ID");
```

Run `mvn test`. Check failure-reports/ folder. A JSON file should appear.

**Exercise:** Add `onTestSuccess` to also write a success_reports/ JSON (simpler — just test name + timestamp). This will be useful for tracking test history later.

**Expected output:** JSON failure report appearing in failure-reports/ automatically when a test fails.

---

### DAY 13 — Python Reading Java Output

**Concept:** os.listdir(), os.path.join(), watching a folder. How Python reads the JSON files that Java just wrote. This is the moment the two languages shake hands.

**Why it matters:** This is the exact mechanism the Watcher uses. Master it today and the Watcher build on Day 21 will feel natural.

**Task — Create `watch_failures.py`:**
```python
import json
import os
import time

FAILURE_REPORTS_DIR = "../sdet-ai-framework/failure-reports/"

def read_all_failures(directory: str) -> list:
    """Read all JSON failure reports from the Java listener output folder."""
    failures = []
    
    if not os.path.exists(directory):
        print(f"Directory not found: {directory}")
        return failures
    
    for filename in os.listdir(directory):
        if filename.endswith(".json"):
            filepath = os.path.join(directory, filename)
            try:
                with open(filepath, "r") as f:
                    failure_data = json.load(f)
                failures.append(failure_data)
                print(f"Read: {filename}")
            except Exception as e:
                print(f"Error reading {filename}: {e}")
    
    return failures

def print_failure_report(failure: dict):
    """Print a human-readable failure summary."""
    print("\n" + "="*50)
    print(f"Test:      {failure.get('test_name', 'unknown')}")
    print(f"Error:     {failure.get('error_type', 'unknown')}")
    print(f"Message:   {failure.get('error_message', '')[:100]}")
    print(f"Time:      {failure.get('timestamp', '')}")
    print(f"Screenshot:{failure.get('screenshot_path', '')}")
    print("="*50)

# Main execution
failures = read_all_failures(FAILURE_REPORTS_DIR)
print(f"\nTotal failures found: {len(failures)}")
for failure in failures:
    print_failure_report(failure)
```

Run your Java tests with the broken locator. Then run watch_failures.py. You should see Python reading and printing the failures Java wrote.

**Exercise 1:** Add a function `get_failures_by_type(failures, error_type)` that filters failures by error type.

**Exercise 2:** Add a function `watch_live(directory, interval=5)` that checks for new files every 5 seconds using a while loop and `time.sleep(5)`. When a new file appears, print it immediately. This is a primitive version of what the Watcher will do.

**Expected output:** Python printing failure summaries from Java's JSON files. Two languages communicating cleanly.

---

### DAY 14 — Deliberately Break All Failure Types + Verify Pipeline

**Concept:** Controlled breakage as a testing technique for testing agents. Why you need to produce all 5 failure types intentionally so the Classifier can be trained and tested properly.

**Why it matters:** The Classifier needs real examples of all 5 failure types to classify accurately. If you only ever have locator failures, the agent will never encounter timing or data failures — and will be untested for them.

**Task:** Introduce each failure type deliberately:

```
Failure Type 1 — LOCATOR:
Change LOGIN_BUTTON locator from By.id("login-button") to By.id("WRONG_ID")
Run tests → testValidLogin fails with NoSuchElementException
Fix it back after confirming JSON appears

Failure Type 2 — TIMING:
Add Thread.sleep(-1) before clickLoginButton() call
Run tests → fails with exception
OR: Remove WebDriverWait from BasePage temporarily

Failure Type 3 — DATA:
Change "secret_sauce" to "wrong_password" in testValidLogin
Run tests → testValidLogin fails with assertion error on error message

Failure Type 4 — GENUINE BUG:
Change assertion to Assert.assertTrue(false, "Simulated genuine bug")
Run tests → fails with AssertionError

Failure Type 5 — ENVIRONMENT:
Change base URL to "https://notarealsite12345.com"
Run tests → fails with connection error
```

For each failure type:
1. Break the test
2. Run `mvn test`
3. Confirm JSON appears in failure-reports/
4. Run `watch_failures.py` and confirm Python reads it
5. Fix the test before introducing next failure type

**Exercise:** Write a Python script `classify_all.py` that reads all failure JSONs and calls your structured_classifier.py for each one. Print the classification result for every failure. At the end of Day 14 you should have 5 failure JSONs classified by the AI.

**Expected output:** 5 different failure types produced, JSON captured, Python reading all, basic AI classification working on real failure data. MILESTONE 1 COMPLETE.

---

## PHASE 2 — THE WATCHER (Days 15–22)
**Goal:** Build the complete failure context collector.
**Milestone:** Day 22 — Watcher collecting complete context automatically on every failure.

---

### DAY 15 — Watcher Architecture + AgentState Design

**Concept:** What is an AgentState — the shared data object that flows through every component of the agent. Think of it as a baton in a relay race — each component adds information to it and passes it forward. Dataclasses in Python — a clean way to define data structures with typed fields.

**Why it matters:** Every component reads from and writes to AgentState. If the structure is unclear, the whole pipeline breaks. Design this carefully today and you will never fight data structure issues later.

**Task — Create `core/state.py`:**
```python
from dataclasses import dataclass, field
from typing import Optional

@dataclass
class AgentState:
    # === INPUT ===
    test_name: str = ""
    test_class: str = ""
    description: str = ""
    page_url: str = ""
    timestamp: str = ""
    
    # === FROM STACK TRACE PARSER ===
    error_type: str = ""
    error_message: str = ""
    stack_trace: str = ""
    failing_line: str = ""
    failing_method: str = ""
    
    # === FROM SCREENSHOT ANALYSER ===
    screenshot_path: str = ""
    screenshot_analysis: str = ""
    
    # === FROM DOM COLLECTOR ===
    live_dom: str = ""
    
    # === FROM TEST CODE READER ===
    test_code: str = ""
    
    # === FROM FAILURE HISTORY ===
    failure_history: list = field(default_factory=list)
    failure_count: int = 0
    
    # === FILLED BY CLASSIFIER ===
    failure_type: str = ""
    confidence: float = 0.0
    reasoning: str = ""
    recommended_action: str = ""
    needs_human_review: bool = False
    
    # === FILLED BY FIXER ===
    fix_applied: str = ""
    fix_outcome: str = ""
    retry_count: int = 0
    patched_file_path: str = ""
    
    # === FINAL REPORT ===
    report_path: str = ""
    
    def to_dict(self) -> dict:
        """Convert to dictionary for JSON serialisation."""
        from dataclasses import asdict
        return asdict(self)
    
    def get_classifier_context(self) -> str:
        """Return a formatted string with all context the Classifier needs."""
        return f"""
Test Name: {self.test_name}
Description: {self.description}
Error Type: {self.error_type}
Error Message: {self.error_message}
Stack Trace (first 5 lines):
{self.stack_trace[:500]}
Screenshot Analysis: {self.screenshot_analysis}
Live DOM (condensed): {self.live_dom[:1000]}
Test Code:
{self.test_code[:500]}
Failure History: Failed {self.failure_count} times previously
        """.strip()
```

**Exercise 1:** Add a method `is_recurring()` that returns True if failure_count is greater than 2.

**Exercise 2:** Add a method `get_severity()` that returns "HIGH" if failure_type is GENUINE_BUG, "MEDIUM" for LOCATOR or TIMING, and "LOW" for DATA or ENVIRONMENT.

**Expected output:** AgentState class working, methods tested in a simple script.

---

### DAY 16 — Stack Trace Parser

**Concept:** Anatomy of a Java stack trace. What each line means. How to extract signal from noise — the first line is the exception type, the second is the message, the `at` lines show the call chain.

**Task — Create `agents/watcher/stack_trace_parser.py`:**
```python
class StackTraceParser:
    """Extracts structured information from Java stack traces."""
    
    def __init__(self, stack_trace: str, error_message: str):
        self.stack_trace = stack_trace
        self.error_message = error_message
        self.lines = [l.strip() for l in stack_trace.split("\n") if l.strip()]
    
    def get_exception_type(self) -> str:
        for line in self.lines:
            if "Exception" in line or "Error" in line:
                if "at " not in line:
                    return line.split(":")[ 0].strip()
        return "UnknownException"
    
    def get_failing_line(self) -> str:
        for line in self.lines:
            if "at " in line and ".java:" in line:
                if "sun." not in line and "java." not in line:
                    return line.replace("at ", "").strip()
        return ""
    
    def get_failing_method(self) -> str:
        failing_line = self.get_failing_line()
        if failing_line:
            parts = failing_line.split("(")
            if parts:
                method_parts = parts[0].split(".")
                return method_parts[-1] if method_parts else ""
        return ""
    
    def get_root_cause(self) -> str:
        cause_lines = [l for l in self.lines if "Caused by:" in l]
        if cause_lines:
            return cause_lines[0].replace("Caused by:", "").strip()
        return self.error_message[:200]
    
    def get_summary(self) -> dict:
        return {
            "exception_type": self.get_exception_type(),
            "failing_line": self.get_failing_line(),
            "failing_method": self.get_failing_method(),
            "root_cause": self.get_root_cause()
        }
```

**Exercise:** Test with 3 different stack traces — one NoSuchElementException, one TimeoutException, one AssertionError. Verify all fields parse correctly.

---

### DAY 17 — Screenshot Analyser

**Concept:** Sending images to Claude using the Anthropic API vision capability. How to read an image file as base64. Why visual context changes classification — a screenshot can show the element IS visible (timing issue) vs completely absent (locator issue).

**Task — Create `agents/watcher/screenshot_analyser.py`:**
```python
import anthropic
import base64
import os

class ScreenshotAnalyser:
    """Sends failure screenshots to Claude for visual analysis."""
    
    def __init__(self):
        self.client = anthropic.Anthropic()
    
    def analyse(self, screenshot_path: str, test_name: str) -> str:
        """Send screenshot to Claude and get visual context."""
        if not screenshot_path or not os.path.exists(screenshot_path):
            return "No screenshot available for analysis."
        
        with open(screenshot_path, "rb") as f:
            image_data = base64.standard_b64encode(f.read()).decode("utf-8")
        
        message = self.client.messages.create(
            model="claude-sonnet-4-6",
            max_tokens=500,
            messages=[{
                "role": "user",
                "content": [
                    {
                        "type": "image",
                        "source": {
                            "type": "base64",
                            "media_type": "image/png",
                            "data": image_data
                        }
                    },
                    {
                        "type": "text",
                        "text": f"""This is a screenshot taken when a Selenium test failed.
Test name: {test_name}
Describe:
1. What page is shown?
2. Is the page fully loaded?
3. Are there any visible error messages?
4. Is the main content/form visible?
5. What might have caused the test failure based on what you see?
Be concise — 3 to 5 sentences maximum."""
                    }
                ]
            }]
        )
        return message.content[0].text
```

**Exercise:** Run this against one of your real failure screenshots from Day 14. Print what Claude says. Does it match what you already know about the failure?

---

### DAY 18 — DOM Collector

**Concept:** Using Selenium in Python to visit a URL and capture page_source. Why raw HTML is too large to send to an LLM — a typical page is 5,000 to 50,000 lines. BeautifulSoup — parsing HTML and extracting only what matters. Condensing DOM to under 2,000 characters.

**Task — Create `agents/watcher/dom_collector.py`:**
```python
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from webdriver_manager.chrome import ChromeDriverManager
from bs4 import BeautifulSoup
import time

class DOMCollector:
    """Visits a live page and extracts condensed interactive element DOM."""
    
    def collect(self, url: str) -> str:
        """Visit URL and return condensed DOM of interactive elements only."""
        driver = None
        try:
            driver = webdriver.Chrome(
                service=Service(ChromeDriverManager().install())
            )
            driver.get(url)
            time.sleep(2)  # Allow page to load
            
            page_source = driver.page_source
            return self._condense_dom(page_source)
            
        except Exception as e:
            return f"DOM collection failed: {str(e)}"
        finally:
            if driver:
                driver.quit()
    
    def _condense_dom(self, html: str) -> str:
        """Extract only interactive elements from full HTML."""
        soup = BeautifulSoup(html, "html.parser")
        
        elements = []
        
        # Get all interactive elements with their key attributes
        interactive_tags = ["input", "button", "a", "select", "textarea", "form"]
        
        for tag in interactive_tags:
            for element in soup.find_all(tag):
                attrs = {}
                for attr in ["id", "name", "class", "type", "placeholder",
                             "data-test", "aria-label", "href", "value"]:
                    val = element.get(attr)
                    if val:
                        if isinstance(val, list):
                            val = " ".join(val)
                        attrs[attr] = val[:50]
                
                text = element.get_text(strip=True)[:30]
                if text:
                    attrs["text"] = text
                
                if attrs:
                    elements.append(f"<{tag} {attrs}>")
        
        condensed = "\n".join(elements)
        
        # Truncate if still too long
        if len(condensed) > 2000:
            condensed = condensed[:2000] + "\n... (truncated)"
        
        return condensed if condensed else "No interactive elements found"
```

**Exercise:** Run DOMCollector against saucedemo.com. Print the condensed DOM. Count how many elements it extracted vs the full page_source length. The difference is what you're saving the LLM from having to process.

---

### DAY 19 — Test Code Reader

**Concept:** Reading Java files in Python. How to extract one specific test method from a file that contains many. String searching, line-by-line reading, tracking brace depth to find where a method ends.

**Task — Create `agents/watcher/test_code_reader.py`:**
```python
import os

class TestCodeReader:
    """Extracts the failing test method code from Java test files."""
    
    def __init__(self, framework_path: str):
        self.framework_path = framework_path
    
    def read_test_method(self, test_name: str, test_class: str) -> str:
        """Extract the specific failing test method from the Java file."""
        java_file = self._find_java_file(test_class)
        if not java_file:
            return f"Could not find Java file for class: {test_class}"
        
        return self._extract_method(java_file, test_name)
    
    def _find_java_file(self, class_name: str) -> str:
        """Find the Java file for a given class name."""
        simple_name = class_name.split(".")[-1]
        for root, dirs, files in os.walk(self.framework_path):
            for file in files:
                if file == f"{simple_name}.java":
                    return os.path.join(root, file)
        return ""
    
    def _extract_method(self, file_path: str, method_name: str) -> str:
        """Extract a specific method from a Java file."""
        with open(file_path, "r") as f:
            lines = f.readlines()
        
        method_lines = []
        inside_method = False
        brace_count = 0
        
        for line in lines:
            if not inside_method:
                if f"void {method_name}(" in line or f"public {method_name}(" in line:
                    inside_method = True
                    method_lines.append(line)
                    brace_count = line.count("{") - line.count("}")
            else:
                method_lines.append(line)
                brace_count += line.count("{") - line.count("}")
                if brace_count <= 0:
                    break
        
        if method_lines:
            return "".join(method_lines)
        return f"Method {method_name} not found in {file_path}"
```

**Exercise:** Test by extracting `testValidLogin` and `testInvalidPassword` from LoginTests.java. Print both. Verify the extracted code matches what you see in the file.

---

### DAY 20 — Failure History Tracker

**Concept:** Persistent storage using JSON files. How tracking failure history changes agent behaviour — a test that failed 5 times is more important than one that failed once. Timestamps and sorting.

**Task — Create `agents/watcher/failure_history_tracker.py`:**
```python
import json
import os
from datetime import datetime

HISTORY_FILE = "output/failure_history.json"

class FailureHistoryTracker:
    """Tracks failure history for each test across runs."""
    
    def __init__(self, history_file: str = HISTORY_FILE):
        self.history_file = history_file
        self.history = self._load_history()
    
    def _load_history(self) -> dict:
        if os.path.exists(self.history_file):
            with open(self.history_file, "r") as f:
                return json.load(f)
        return {}
    
    def _save_history(self):
        os.makedirs(os.path.dirname(self.history_file), exist_ok=True)
        with open(self.history_file, "w") as f:
            json.dump(self.history, f, indent=2)
    
    def record_failure(self, test_name: str, error_type: str, fix_outcome: str = "pending"):
        if test_name not in self.history:
            self.history[test_name] = []
        
        self.history[test_name].append({
            "timestamp": datetime.now().isoformat(),
            "error_type": error_type,
            "fix_outcome": fix_outcome
        })
        self._save_history()
    
    def get_history(self, test_name: str) -> list:
        return self.history.get(test_name, [])
    
    def get_failure_count(self, test_name: str) -> int:
        return len(self.history.get(test_name, []))
    
    def get_history_summary(self, test_name: str) -> str:
        history = self.get_history(test_name)
        if not history:
            return "No previous failures recorded."
        count = len(history)
        last = history[-1]
        return f"Failed {count} time(s). Last failure: {last['error_type']} on {last['timestamp'][:10]}"
```

**Exercise:** Simulate 3 failures for testValidLogin and 1 failure for testInvalidPassword. Print history summaries for both. Verify counts are correct after reading from the saved JSON file.

---

### DAY 21 — Watcher Orchestrator

**Concept:** Orchestration pattern — one class that coordinates multiple sub-components and produces a single clean output. Why the Classifier should receive one perfectly packaged object rather than 5 separate calls.

**Task — Create `agents/watcher/watcher.py`:**
```python
import json
import os
from core.state import AgentState
from agents.watcher.stack_trace_parser import StackTraceParser
from agents.watcher.screenshot_analyser import ScreenshotAnalyser
from agents.watcher.dom_collector import DOMCollector
from agents.watcher.test_code_reader import TestCodeReader
from agents.watcher.failure_history_tracker import FailureHistoryTracker

FAILURE_REPORTS_DIR = "../sdet-ai-framework/failure-reports/"
FRAMEWORK_PATH = "../sdet-ai-framework/src"

class Watcher:
    """Orchestrates all context collectors and builds complete AgentState."""
    
    def __init__(self):
        self.screenshot_analyser = ScreenshotAnalyser()
        self.dom_collector = DOMCollector()
        self.code_reader = TestCodeReader(FRAMEWORK_PATH)
        self.history_tracker = FailureHistoryTracker()
    
    def watch(self) -> list:
        """Read all pending failure reports and build AgentState for each."""
        states = []
        for filename in os.listdir(FAILURE_REPORTS_DIR):
            if filename.endswith(".json"):
                filepath = os.path.join(FAILURE_REPORTS_DIR, filename)
                state = self._build_state(filepath)
                if state:
                    states.append(state)
        return states
    
    def _build_state(self, failure_json_path: str) -> AgentState:
        with open(failure_json_path, "r") as f:
            raw = json.load(f)
        
        state = AgentState(
            test_name=raw.get("test_name", ""),
            test_class=raw.get("test_class", ""),
            description=raw.get("description", ""),
            page_url=raw.get("page_url", ""),
            timestamp=raw.get("timestamp", ""),
            error_type=raw.get("error_type", ""),
            error_message=raw.get("error_message", ""),
            stack_trace=raw.get("stack_trace", ""),
            screenshot_path=raw.get("screenshot_path", "")
        )
        
        # Parse stack trace
        parser = StackTraceParser(state.stack_trace, state.error_message)
        summary = parser.get_summary()
        state.failing_line = summary["failing_line"]
        state.failing_method = summary["failing_method"]
        
        # Analyse screenshot
        print(f"  Analysing screenshot for {state.test_name}...")
        state.screenshot_analysis = self.screenshot_analyser.analyse(
            state.screenshot_path, state.test_name
        )
        
        # Collect live DOM
        if state.page_url:
            print(f"  Collecting live DOM from {state.page_url}...")
            state.live_dom = self.dom_collector.collect(state.page_url)
        
        # Read test code
        print(f"  Reading test code for {state.test_name}...")
        state.test_code = self.code_reader.read_test_method(
            state.test_name, state.test_class
        )
        
        # Get failure history
        state.failure_history = self.history_tracker.get_history(state.test_name)
        state.failure_count = self.history_tracker.get_failure_count(state.test_name)
        self.history_tracker.record_failure(state.test_name, state.error_type)
        
        return state
```

**Exercise:** Run Watcher.watch() against your Day 14 failure reports. Print the classifier_context from each AgentState. Verify all fields are populated.

---

### DAY 22 — Watcher End-to-End Test + Phase 2 Complete

**Concept:** Integration testing your own component. Running the full Watcher pipeline on real failure data.

**Task:**
1. Break 3 tests intentionally (one locator, one timing, one data failure)
2. Run `mvn test` in the Java framework
3. Run Watcher.watch() in Python
4. Print full AgentState for each failure
5. Verify every field has real data — nothing empty except optional fields

**Exercise:** Write a Watcher validation script that checks each AgentState field is populated and prints a health report:
```
AgentState Validation:
✅ test_name: populated
✅ error_type: populated
✅ stack_trace: populated
✅ screenshot_analysis: populated
✅ live_dom: populated
✅ test_code: populated
⚠️  failure_history: empty (first time this test has failed)
```

**Expected output:** Watcher producing complete, validated AgentState objects for 3 real failures. PHASE 2 MILESTONE COMPLETE.

---

## PHASE 3 — THE CLASSIFIER (Days 23–35)
**Goal:** The AI brain. Classify failure type with confidence.
**Milestone:** Day 30 — AI correctly classifying all 5 failure types end-to-end with Watcher.

---

### DAY 23 — Prompt Engineering Fundamentals

**Concept:** What makes a good system prompt. Role, context, instructions, constraints, output format — the 5 elements of a production prompt. Why vague prompts produce vague results.

**Task:** Write 5 different system prompts for the Classifier. Run each against the same failure. Compare outputs. Document which worked best and why.

**Exercise:** Write a prompt evaluation script that rates each response on: correct failure type, JSON parseable, confidence score present, reasoning present. Score each prompt 0-4.

---

### DAY 24 — Classifier Design and System Prompt

**Concept:** How to define the 5 failure types precisely in a prompt. Why examples in the prompt improve accuracy. Output format specification.

**Task — Create `agents/classifier/classifier_prompt.py`** with the production system prompt including all 5 failure type definitions, examples, and exact JSON output format.

**Exercise:** Test prompt against all 5 failure types from Day 14. Count how many are classified correctly before any refinement.

---

### DAY 25 — Classifier Build + Locator Detection

**Task — Create `agents/classifier/classifier.py`.** Test specifically against 3 locator failures. Verify failure_type: "LOCATOR", confidence > 0.85.

**Exercise:** What happens to confidence when screenshot_analysis is missing vs present? Run same failure both ways. Document the difference.

---

### DAY 26 — Timing Failure Detection

**Task:** Create 3 timing failures (remove waits). Test Classifier. Verify it distinguishes timing from locator when both use similar exceptions.

**Exercise:** Create one ambiguous failure that could be either timing or locator. Check what confidence the Classifier returns. Verify needs_human_review is true if confidence < 0.70.

---

### DAY 27 — Data and Environment Failures

**Task:** Create data failure (wrong credentials) and environment failure (wrong URL). Test both. Verify correct classification and correct recommended_action for each type.

**Exercise:** Write a test where stale data and a broken locator exist simultaneously. Which does the Classifier choose? Is that the right choice?

---

### DAY 28 — Genuine Bug Detection

**Task:** Intentionally break the application feature (wrong assertion expecting correct behaviour). Verify Classifier returns GENUINE_BUG and needs_human_review: true. Verify it does NOT suggest a code fix.

**Exercise:** Write an explanation of why auto-fixing a genuine bug would be dangerous. Write it as if explaining to a junior developer. This prepares your Day 74 interview answer.

---

### DAY 29 — Confidence Scoring + Escalation Handler

**Task — Create `agents/classifier/escalation_handler.py`.** Routes low-confidence classifications to escalation_queue.json instead of the Fixer.

**Exercise:** Create 5 ambiguous failures. Run all through Classifier. Count how many trigger escalation. Is the threshold (0.60) correct or does it need adjustment?

---

### DAY 30 — Full Pipeline + Milestone 2

**Task:** Run complete Watcher → Classifier pipeline. Break one test of each type. Confirm all 5 classified correctly. Print full results.

**Exercise:** Calculate your Classifier's accuracy across all 5 types. What is your percentage? Write it down. You will compare this to Day 33's result after refinement.

**MILESTONE 2 COMPLETE.**

---

### DAYS 31–33 — Classifier Stress Testing

**Task:** Test with 15 different scenarios including edge cases — missing screenshot, truncated stack trace, empty DOM, partial stack trace. Fix every incorrect classification. Target: 90%+ accuracy.

**Exercise each day:** Run 5 new scenarios. Record results in a test log. Classification, confidence, correct or incorrect, what you changed to fix it.

---

### DAYS 34–35 — Classifier Documentation + Cleanup

**Task:** Clean all code. Add docstrings. Write CLASSIFIER.md. Commit to GitHub with message "Phase 3 complete: Classifier achieving 90%+ accuracy across all 5 failure types."

**Exercise:** Re-calculate accuracy on Day 35. Compare to Day 30. Document the improvement and what changes caused it.

---

## PHASE 4 — THE FIXER (Days 36–52)
**Goal:** Five specialist fixers. Complete self-healing loop.
**Milestone:** Day 50 — Complete self-healing demo. YOUR INTERVIEW DEMO.

---

### DAY 36 — Fixer Architecture + Router

**Concept:** Router pattern, Strategy pattern, BaseFixer abstract class.

**Task:** Build FixerRouter + BaseFixer. Route based on failure_type string. Test router sends LOCATOR to LocatorHealer, TIMING to TimingFixer, etc.

**Exercise:** What happens if failure_type is UNKNOWN? Build a fallback — log and escalate.

---

### DAYS 37–39 — Fixer 1: Locator Healer

**Concept:** How to read a Java file, find a specific locator, replace it, write back. Why backup the original.

**Task:** Build LocatorHealer. Uses condensed DOM + test intent → Claude suggests locator → patch Java POM file → save backup.

**Exercise Day 37:** Read and print the failing locator from the Java file.
**Exercise Day 38:** Ask Claude to suggest an alternative. Print suggestion.
**Exercise Day 39:** Patch the file. Verify backup exists. Verify the patched locator is correct.

---

### DAYS 40–41 — Fixer 2: Timing Fixer

**Concept:** WebDriverWait patterns — visibilityOfElementLocated, elementToBeClickable, presenceOfElementLocated.

**Task:** Build TimingFixer. Detects Thread.sleep or missing wait. Replaces with appropriate WebDriverWait.

**Exercise Day 40:** Identify which wait type is needed for each of 3 timing scenarios.
**Exercise Day 41:** Patch 2 different timing failures. Verify fixed code compiles.

---

### DAYS 42–43 — Fixer 3: Data Refresher

**Task:** Build DataRefresher. Detect hardcoded credentials → move to config file → update test to read from config.

**Exercise:** After DataRefresher runs — verify test still passes using the externalised data.

---

### DAYS 44–45 — Fixer 4: Bug Reporter

**Task:** Build BugReporter. Read assertion → generate structured bug report JSON → verify it does NOT modify any test file.

**Exercise:** Generate bug reports for 3 different genuine failures. Verify all contain: title, severity, steps, expected, actual, test name, screenshot path.

---

### DAY 46 — Fixer 5: Environment Handler

**Task:** Build EnvironmentHandler. Retry once after 30s. Log transient vs persistent. Alert if persistent.

**Exercise:** Simulate a transient failure (run once broken, then fix). Verify handler classifies it as transient.

---

### DAYS 47–48 — Execution Engine

**Concept:** Python subprocess module. Parsing TestNG Surefire XML output.

**Task:** Build ExecutionEngine. Run `mvn test -Dtest=TestName` via subprocess. Parse XML result. Return pass/fail + execution time.

**Exercise Day 47:** Run one test via subprocess. Print the raw output.
**Exercise Day 48:** Parse the XML and extract pass/fail status cleanly.

---

### DAY 49 — Retry Loop (ReAct Pattern)

**Concept:** ReAct = Reason → Act → Observe → Repeat. Max 3 retries. Detect if fix made things worse.

**Task:** Build RetryLoop. After fix → re-run → if fail → send updated context to Classifier → try again → max 3 attempts.

**Exercise:** Simulate a failure that requires 2 repair attempts (fix a locator, but then another locator is wrong). Verify the loop handles it correctly and stops at 3.

---

### DAY 50 — Full Pipeline + Milestone 3

**Task:** Run complete end-to-end pipeline. Break one test of each type. Watch: Java fails → Watcher → Classifier → Fixer → Retry → Report. All automatic. Record a screen video.

**Exercise:** Measure heal success rate across all 5 failure types. Calculate percentage. This number goes in your README.

**MILESTONE 3 COMPLETE — YOUR INTERVIEW DEMO.**

---

### DAYS 51–52 — Fixer Stress Testing + Cleanup

**Task:** Run 15 total healing cycles. Document each. Target 80%+ heal rate. Clean code. Write FIXERS.md. Push to GitHub.

**Exercise:** What failure scenarios does the healer struggle with? Document them honestly. This becomes your "what would you improve" interview answer.

---

## PHASE 5 — THE PREDICTOR (Days 53–62)
**Goal:** Proactive fragility detection before tests break.
**Milestone:** Day 62 — Predictor identifying fragile tests before they break.

---

### DAY 53 — Fragility Concept

**Task:** Manually score all 9 tests for fragility 1-10 using your own judgement. Document reasoning.

**Exercise:** Rank tests from most fragile to least. Save as manual_assessment.json. This is ground truth you verify the Predictor against on Day 61.

---

### DAYS 54–55 — Code Analyser

**Task:** Build CodeAnalyser. Detect Thread.sleep, dynamic XPaths, hardcoded strings, missing waits, deeply nested locators in Java files.

**Exercise Day 54:** Detect Thread.sleep calls across all test files.
**Exercise Day 55:** Detect all hardcoded string values that look like test data.

---

### DAYS 56–57 — Fragility Scorer

**Task:** Build FragilityScorer. Send detected signals + test code to Claude → get fragility_score, risk_factors, recommendations.

**Exercise Day 56:** Score 3 tests. Compare AI score to your manual score from Day 53.
**Exercise Day 57:** Score all 9 tests. Calculate correlation between AI scores and manual scores.

---

### DAYS 58–59 — Prediction Report Generator

**Task:** Build PredictionReportGenerator. Rank tests by fragility. Generate prediction_report.json with priority queue.

**Exercise:** Generate the report. Identify top 3 most fragile tests. These should match the tests that broke most during your Phase 4 testing.

---

### DAYS 60–61 — Predictor Validation

**Task:** Break the 3 tests the Predictor flagged as most fragile. Verify they were indeed the most brittle. Adjust scoring if needed.

**Exercise:** Calculate prediction accuracy. Did the Predictor correctly identify fragile tests? Document the result.

---

### DAY 62 — Cleanup + Milestone 4

**Task:** Clean code. Write PREDICTOR.md. Commit. Milestone 4 complete.

**Exercise:** Write a plain English explanation of how the Predictor works that a non-technical manager would understand. Practice saying it out loud.

---

## PHASE 6 — THE MEMORY (Days 63–70)
**Goal:** RAG layer — agent learns from experience.
**Milestone:** Day 70 — Agent provably smarter with memory than without.

---

### DAY 63 — RAG Deep Dive (Concept Only — No Code)

**Concept:** Embeddings — text converted to vectors. Vector similarity — how ChromaDB finds similar documents. Why RAG makes agents smarter over time. Read, understand, draw a diagram. No code today.

**Exercise:** Without any library — take 5 of your failure summaries. Manually group them by similarity. This is exactly what the vector DB will automate.

---

### DAY 64 — ChromaDB Setup + First Embeddings

**Task:** Install ChromaDB and sentence-transformers. Create failure_memory collection. Embed 5 failure summaries. Query for similar failures.

**Exercise:** Query "element not found after page load" — does it retrieve the timing failure or the locator failure? Why? Is that the correct answer?

---

### DAY 65 — Memory Writer

**Task:** Build MemoryWriter. After every agent cycle — store failure summary + classification + fix + outcome in ChromaDB.

**Exercise:** Run 5 complete agent cycles. Verify 5 records in ChromaDB. Query for one of them by description and confirm retrieval.

---

### DAY 66 — Memory Reader

**Task:** Build MemoryReader. Given new failure — retrieve 3 most similar past failures. Format as context string for Classifier.

**Exercise:** Manually verify the 3 retrieved records are genuinely similar to the query. If not — adjust the embedding approach.

---

### DAYS 67–68 — Memory Integration + Smarter Classifier

**Task:** Inject MemoryReader context into Classifier prompt. Run 10 cycles with memory vs 10 without. Compare accuracy.

**Exercise Day 67:** Run Classifier on 10 failures without memory. Record accuracy.
**Exercise Day 68:** Run same 10 failures with memory. Record accuracy. Calculate improvement percentage.

---

### DAYS 69–70 — Memory Hygiene + Milestone 5

**Task:** Keep only last 100 records per failure type. Write MEMORY.md. Push to GitHub. Milestone 5 complete.

**Exercise:** Write the "what I learned" paragraph for your README about the RAG implementation. This will be used on Day 73.

---

## PHASE 7 — POLISH + PORTFOLIO (Days 71–75)
**Goal:** Turn working project into interview-ready portfolio.
**Milestone:** Day 75 — Done. Portfolio ready. Interview ready.

---

### DAY 71 — HTML Report Generator

**Task:** Build ReportGenerator using Jinja2. Generate clean HTML report after every run — tests run, classifications, fixes, heal rate, fragility scores.

**Exercise:** Show the report to someone who is not a developer. Can they understand what happened? If not — simplify the language.

---

### DAY 72 — Streamlit Dashboard

**Task:** Build app.py. Shows: test suite health, recent failures + classifications, heal rate over time, top 3 fragile tests.

**Exercise:** Screenshot the dashboard. This goes in your GitHub README.

---

### DAY 73 — GitHub README

**Task:** Write complete README.md — problem statement (2 sentences), architecture, tech stack, how to run, demo screenshot, what you learned.

**Exercise:** Read your README as if you are an interviewer seeing it for the first time. What questions do you have? Answer them in the README.

---

### DAY 74 — Demo Video + Interview Prep

**Task:** Record 3 minute demo — break a test, watch SDET.ai heal it, show report, show fragility prediction. Practise answers out loud.

**Key answers to practise:**
- "Tell me about a project you built" — 90 seconds
- "Why did you build it this way?" — 60 seconds
- "How does the AI work?" — 90 seconds
- "What is the most impressive part?" — 60 seconds
- "What would you improve?" — 60 seconds

**Exercise:** Record yourself answering each question. Play it back. Are you clear? Are you confident? Fix what sounds uncertain.

---

### DAY 75 — Final Review — You Are Done

**Task:** Final end-to-end run. All 5 failure types. All 5 components. Clean report. Clean dashboard. Clean GitHub. Read README one more time. Fix anything unclear.

**Final exercise:** Say this out loud and make sure you believe it:
*"I built SDET.ai — an intelligent test maintenance agent that classifies Selenium failures, heals broken tests, predicts fragility, and learns from experience using RAG. It is built in Python on top of a Java Selenium framework because each language does what it is best at. I understand every line of code in this project."*

If you believe it — you are ready.

**SDET.ai COMPLETE. PORTFOLIO READY. INTERVIEW READY.**

---

## SECTION 7 — KEY TECHNICAL REFERENCE

### LLM Client
```python
import anthropic
client = anthropic.Anthropic()

def call_llm(system_prompt: str, user_prompt: str) -> str:
    message = client.messages.create(
        model="claude-sonnet-4-6",
        max_tokens=1000,
        system=system_prompt,
        messages=[{"role": "user", "content": user_prompt}]
    )
    return message.content[0].text
```

### Python-Java Integration
```python
import subprocess
result = subprocess.run(
    ['mvn', 'test', f'-Dtest={test_name}'],
    cwd='../sdet-ai-framework',
    capture_output=True, text=True
)
```

### RAG Pipeline
```
Text → sentence-transformers → vector → ChromaDB
Query → sentence-transformers → vector → similarity search → top 3 → inject into prompt
```

### requirements.txt
```
anthropic
selenium
webdriver-manager
beautifulsoup4
chromadb
sentence-transformers
streamlit
jinja2
requests
```

---

## SECTION 8 — HOW CLAUDE SHOULD INTERACT

### Tone
Conversational, honest, direct. Not formal. Not stiff. Talk like a mentor who respects him.

### Rules
1. One concept at a time. Never dump multiple things at once.
2. Ask before explaining — what does he already know?
3. Check understanding before moving forward.
4. When stuck — ask "what have you tried?" before giving the answer.
5. Never rush him. The roadmap is a guide, not a deadline.
6. When an error is pasted — respond in order: what it means, why it happened, how to fix it, how to prevent it.
7. When a milestone is reached — acknowledge it clearly. Tell him what he achieved and why it matters.
8. Never skip a day. Never jump ahead.

### Daily Session Structure
**Start:** Recap yesterday → introduce today's concept → give today's task
**During:** Answer questions, debug errors, check understanding
**End:** Confirm output achieved → remind to commit to GitHub → brief preview of tomorrow

### When He Returns After a Break
Do not guilt-trip. Pick up exactly where he left off. Ask: "Where did we leave off — did you complete Day X or are we continuing from there?"

---

## SECTION 9 — CURRENT STATUS

- **Day 1 not yet completed.** Environment checks not yet run.
- Project direction is fully finalised. No more changes to scope.
- He is ready to start when environment is verified.

### What To Do When He Opens This File in a New Session
1. Read the entire file first
2. Greet him naturally — do not announce "I have read your context file"
3. Ask: "Did you get the environment checks done, or are we starting fresh on Day 1?"
4. Continue from exactly where he is

### Signal That Day 1 Is Complete
He will say: **"All 7 checks passed. Repo created."**
When you see this — confirm Day 1 complete and immediately start Day 2.

---

## SECTION 10 — INTERVIEW ANSWERS

**"Tell me about a project you built."**
SDET.ai — an intelligent test maintenance agent. It watches a Java Selenium framework, classifies test failures using AI, heals them automatically, and predicts which tests will break next. Built in Python with Claude API, ChromaDB for RAG, and LangGraph for orchestration.

**"Why did you build it this way?"**
Test maintenance is the #1 pain in Selenium automation. Existing tools treat every failure as a locator problem. SDET.ai classifies the failure type first — then routes to the right action. That classification step is the entire intelligence of the system.

**"How does the AI work?"**
The Classifier sends the complete failure context — stack trace, screenshot, live DOM, test code, and failure history — to Claude simultaneously. It reasons across all signals and returns a structured JSON with failure type, confidence score, and reasoning. It is not pattern matching. It is multi-signal reasoning.

**"Most impressive part?"**
Genuine bug detection. When the AI identifies a real feature bug — it does NOT attempt to fix the test. It generates a bug report and escalates. Knowing when not to act is the hardest thing to encode in an agent. Most tools get this wrong.

**"What would you improve?"**
Support for Cypress and Playwright. Fine-tuning the classifier on company-specific failure patterns. Slack integration for real-time alerts.

**"What did you learn?"**
Context engineering is 80% of AI work. Getting the right information to the LLM, in the right format, at the right time — that is what determines quality. The model call itself is the easy part.

---

*End of SDET.ai Complete Context File — Version 2.0*
*Every day planned. Every concept mapped. Every exercise written.*
*Read this file completely before every session. Never skim.*
