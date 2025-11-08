# 🤖 Smart Task Generator - Intelligent Task List Generator

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-brightgreen)
![Spring AI](https://img.shields.io/badge/Spring%20AI-1.0.0--M3-blue)
![Maven](https://img.shields.io/badge/Maven-3.9+-red)
![Docker](https://img.shields.io/badge/Docker-Ready-blue)
![Ollama](https://img.shields.io/badge/Ollama-Supported-green)

> ⚡ **New!** Now with local LLM support (Ollama) and Makefile for easier development!

## 📋 Description

**Smart Task Generator** is a Java application that transforms a general objective into a clear, organized, and structured task list using Artificial Intelligence through Spring AI.

### ✨ Features

- 🎯 **Intelligent Decomposition**: Transforms complex objectives into practical tasks
- 📊 **Automatic Structuring**: Organizes tasks with priorities and estimates
- 🔗 **Dependency Identification**: Detects relationships between tasks
- 🤖 **Local or Cloud LLM**: Works with OpenAI or Ollama (local)
- 🌐 **REST API**: Easy to integrate interface
- 📖 **Swagger Documentation**: Fully documented API
- 🐳 **Docker Ready**: Easy deployment with containers
- 🛠️ **Makefile**: Simplified commands for development

## 🏗️ Architecture

```
smart-task-generator-java/
├── src/
│   ├── main/
│   │   ├── java/com/taskgenerator/
│   │   │   ├── SmartTaskGeneratorApplication.java
│   │   │   ├── controller/
│   │   │   │   └── TaskGeneratorController.java
│   │   │   ├── service/
│   │   │   │   └── TaskGeneratorService.java
│   │   │   └── dto/
│   │   │       ├── TaskGenerationRequest.java
│   │   │       └── TaskGenerationResponse.java
│   │   └── resources/
│   │       ├── application.yml
│   │       ├── application-dev.yml
│   │       └── application-prod.yml
│   └── test/
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md
```

## 🚀 Technologies

- **Java 17**: Programming language
- **Spring Boot 3.2.0**: Base framework
- **Spring AI**: Integration with AI models
- **OpenAI GPT**: Cloud language model (optional)
- **Ollama**: Local LLM (automatic fallback)
- **Hugging Face**: Support for alternative models
- **Maven**: Dependency management
- **Docker**: Containerization
- **Swagger/OpenAPI**: API documentation

## 📦 Prerequisites

- Java 17 or higher
- Maven 3.9 or higher
- Docker and Docker Compose (optional, for running with containers)
- Make (optional, for using simplified commands)
- **Ollama** (required if not using OpenAI) - [Installation instructions below](#-installing-ollama)
- OpenAI API Key (optional - uses local Ollama as fallback)

## ⚙️ Configuration

### 1. Clone the repository

```bash
git clone https://github.com/seu-usuario/smart-task-generator-java.git
cd smart-task-generator-java
```

### 2. Configure environment variables

Create the `.env` file from the example:

```bash
cp env-example.txt .env
# ou
make env-setup
```

Edit the `.env` file according to your choice:

#### **Option A: Use OpenAI (Cloud)** ☁️

```env
# OpenAI Configuration
OPENAI_API_KEY=sk-your-key-here
USE_LOCAL_LLM=false
```

> **How to get the OpenAI key:**
>
> 1. Visit [platform.openai.com/api-keys](https://platform.openai.com/api-keys)
> 2. Create a new API key
> 3. Paste the key in the `.env` file

#### **Option B: Use Ollama (Local - Free)** 🤖

```env
# OpenAI Configuration (leave empty)
OPENAI_API_KEY=

# Ollama Configuration
USE_LOCAL_LLM=true
OLLAMA_BASE_URL=http://localhost:11434        # To run locally with 'make dev'
# OLLAMA_BASE_URL=http://host.docker.internal:11434  # To run in Docker with 'make up'
OLLAMA_MODEL=phi
```

> **⚠️ IMPORTANT:**
>
> - Use `http://localhost:11434` when running with `make dev` or `mvn spring-boot:run`
> - Use `http://host.docker.internal:11434` when running with `make up` (Docker)
> - The system automatically **falls back** to Ollama if the OpenAI key is not configured

#### **.env file variables:**

| Variable                 | Description                 | Default Value                       |
| ------------------------ | --------------------------- | ----------------------------------- |
| `OPENAI_API_KEY`         | OpenAI API Key (optional)   | -                                   |
| `HUGGINGFACE_API_KEY`    | Hugging Face Key (optional) | -                                   |
| `USE_LOCAL_LLM`          | Force use of Ollama         | `false`                             |
| `OLLAMA_BASE_URL`        | Ollama server URL           | `http://host.docker.internal:11434` |
| `OLLAMA_MODEL`           | Ollama model to use         | `phi`                               |
| `SPRING_PROFILES_ACTIVE` | Spring Profile (dev/prod)   | `dev`                               |
| `SERVER_PORT`            | Application port            | `8080`                              |

### 3. 🤖 Installing Ollama

If you chose to use Ollama (local LLM), follow the steps below:

#### **macOS:**

```bash
# Via Homebrew
brew install ollama

# Ou baixe o instalador em:
# https://ollama.ai/download
```

#### **Linux:**

```bash
curl -fsSL https://ollama.ai/install.sh | sh
```

#### **Windows:**

Download the installer at: https://ollama.ai/download

#### **After installation:**

```bash
# 1. Start the Ollama server (in a separate terminal)
ollama serve

# 2. Download the phi model (or another of your preference)
ollama pull phi

# 3. Check installed models
ollama list

# Or use the Makefile:
make ollama-start  # Starts the server
make ollama-pull   # Downloads the phi model
```

> **💡 Tip:** Keep `ollama serve` running in a separate terminal while developing.

#### **Recommended Ollama Models:**

| Model       | Size  | Description             | Command                 |
| ----------- | ----- | ----------------------- | ----------------------- |
| `phi`       | 1.6GB | Default (leve e rápido) | `ollama pull phi`       |
| `llama2`    | 3.8GB | Balanced, good speed    | `ollama pull llama2`    |
| `mistral`   | 4.1GB | Better response quality | `ollama pull mistral`   |
| `codellama` | 3.8GB | Specialized in code     | `ollama pull codellama` |

### 4. Compile the project

```bash
# With Maven
mvn clean install

# Or with Makefile
make install
```

## 🏃 Running the Application

### 🚀 Quick Start with Makefile (Recommended)

```bash
# View all available commands
make help

# Configure and start quickly
make quick-start

# Or start in development mode
make dev

# Check application health
make health

# Test API
make api-test
```

### Option 1: Run with Maven

```bash
mvn spring-boot:run
# or
make run
```

### Option 2: Run with Docker

```bash
# Start containers
docker-compose up -d

# Or using Makefile
make up

# View logs
docker-compose logs -f
# or
make logs

# Stop containers
docker-compose down
# or
make down
```

> **⚠️ Note when using Docker:**
>
> - Configure `OLLAMA_BASE_URL=http://host.docker.internal:11434` in `.env`
> - Make sure Ollama is running on the host: `ollama serve`

The application will be available at: `http://localhost:8080`

## 📚 API Usage

### Endpoint: Generate Tasks

**POST** `/api/v1/tasks/generate`

**Request Body:**

```json
{
  "objective": "Develop a mobile food delivery app",
  "maxTasks": 8,
  "detailLevel": "medium"
}
```

**Response:**

```json
{
  "originalObjective": "Develop a mobile food delivery app",
  "tasks": [
    {
      "order": 1,
      "title": "Define project requirements and scope",
      "description": "Gather stakeholders and define essential features...",
      "priority": "high",
      "estimatedHours": 16,
      "dependencies": []
    },
    {
      "order": 2,
      "title": "Create wireframes and prototypes",
      "description": "Design main screens and user flows...",
      "priority": "high",
      "estimatedHours": 24,
      "dependencies": ["1"]
    }
  ],
  "generatedAt": "2025-11-07T10:30:00",
  "model": "gpt-3.5-turbo"
}
```

### Endpoint: Health Check

**GET** `/api/v1/tasks/health`

**Response:**

```
Smart Task Generator is running!
```

## 📖 Swagger Documentation

Access the interactive API documentation at:

```
http://localhost:8080/swagger-ui.html
```

## 🛠️ Makefile Commands

The project includes a complete Makefile to facilitate development:

### Main Commands

```bash
make help              # Show all available commands
make install           # Install Maven dependencies
make build             # Compile the project
make test              # Run tests
make run               # Run locally
make dev               # Development mode
make clean             # Clean build
```

### Docker

```bash
make docker-build      # Build Docker image
make up                # Start containers
make down              # Stop containers
make logs              # Show logs
make restart           # Restart containers
make docker-clean      # Remove images and containers
```

### Ollama (Local LLM)

```bash
make ollama-start      # Start Ollama
make ollama-stop       # Stop Ollama
make ollama-pull       # Download phi model
```

### Utilities

```bash
make health            # Check app health
make api-test          # Test API with example
make status            # Status of all services
make env-setup         # Create .env file
make quick-start       # Complete quick start
```

## 🤖 Local LLM vs Cloud

### How Automatic Fallback Works

Smart Task Generator implements an intelligent fallback system:

1. **Tries to use OpenAI** if the key is configured
2. **Falls back to Ollama** automatically if:
   - `OPENAI_API_KEY` is not configured
   - `USE_LOCAL_LLM=true` in `.env`
   - Error connecting to OpenAI

### Configuring Local LLM (Ollama)

**Step by Step:**

1. **Install Ollama** (see [installation section](#-installing-ollama))

2. **Start the Ollama server:**

```bash
ollama serve
# Keep this terminal running
```

3. **In another terminal, download a model:**

```bash
ollama pull phi
# or
make ollama-pull
```

4. **Configure the `.env` file:**

```env
# Force Ollama usage
USE_LOCAL_LLM=true

# Ollama server URL
# For local execution (make dev):
OLLAMA_BASE_URL=http://localhost:11434

# For Docker execution (make up):
# OLLAMA_BASE_URL=http://host.docker.internal:11434

# Choose the model
OLLAMA_MODEL=phi  # or mistral, codellama, llama2, etc.
```

5. **Start the application:**

```bash
make dev
```

### Comparison: OpenAI vs Ollama

| Feature       | OpenAI GPT        | Ollama (Local)      |
| ------------- | ----------------- | ------------------- |
| 💰 Cost       | Paid (API)        | Free                |
| 🌐 Connection | Requires internet | Offline             |
| ⚡ Speed      | Fast              | Depends on hardware |
| 🎯 Quality    | Excellent         | Good                |
| 🔒 Privacy    | Cloud             | Fully local         |
| 💻 Hardware   | Not required      | GPU recommended     |

## 🧪 Tests

Run tests with:

```bash
mvn test
```

## 🐳 Docker

### Build the image

```bash
docker build -t smart-task-generator:latest .
```

### Run container

```bash
docker run -p 8080:8080 \
  -e OPENAI_API_KEY=your-key \
  smart-task-generator:latest
```

### Use Docker Compose

```bash
# Start
docker-compose up -d

# View logs
docker-compose logs -f

# Stop
docker-compose down
```

## 📝 Usage Examples

### Example 1: Software Project

```bash
curl -X POST http://localhost:8080/api/v1/tasks/generate \
  -H "Content-Type: application/json" \
  -d '{
    "objective": "Create a library management system",
    "maxTasks": 10,
    "detailLevel": "high"
  }'
```

### Example 2: Event

```bash
curl -X POST http://localhost:8080/api/v1/tasks/generate \
  -H "Content-Type: application/json" \
  -d '{
    "objective": "Organize a technology conference with 500 participants",
    "maxTasks": 15,
    "detailLevel": "medium"
  }'
```

## 🔧 Advanced Settings

### Change the AI model

**OpenAI:**

```yaml
# application.yml
spring:
  ai:
    openai:
      chat:
        options:
          model: gpt-4 # or gpt-3.5-turbo, gpt-4-turbo
          temperature: 0.7
```

**Ollama:**

```yaml
# application.yml or .env
app:
  ai:
    ollama:
      model: phi # or mistral, codellama, llama2, etc.
      base-url: http://localhost:11434
```

### Spring Profiles

- **dev**: Development (detailed logs, GPT-3.5)
- **prod**: Production (reduced logs, GPT-4)

Activate profile:

```bash
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run
# or
make dev
```

### Environment Variables

All settings can be done via `.env`:

```env
# AI Provider
OPENAI_API_KEY=sk-...          # OpenAI Key (optional)
USE_LOCAL_LLM=false            # true = force Ollama

# Ollama
OLLAMA_BASE_URL=http://localhost:11434
OLLAMA_MODEL=phi

# App
SPRING_PROFILES_ACTIVE=dev
SERVER_PORT=8080
```

## 🤝 Contributing

Contributions are welcome! Please:

1. Fork the project
2. Create a branch for your feature (`git checkout -b feature/MyFeature`)
3. Commit your changes (`git commit -m 'Add MyFeature'`)
4. Push to the branch (`git push origin feature/MyFeature`)
5. Open a Pull Request

## 📄 License

This project is under the MIT license. See the [LICENSE](LICENSE) file for more details.

## 👤 Author

**Valter Sales**

- GitHub: [@valtersales](https://github.com/valtersales)
- LinkedIn: [Valter Sales](https://linkedin.com/in/valtersales)

## 🙏 Acknowledgments

- Spring AI Team
- OpenAI
- Hugging Face
- Java Community

## 📞 Support

For questions or suggestions, open an [issue](https://github.com/seu-usuario/smart-task-generator-java/issues) on GitHub.

---

⭐ If this project was useful to you, consider giving it a star on GitHub!
