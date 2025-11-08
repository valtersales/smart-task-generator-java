.PHONY: help build run restart logs clean test install dev docker-build up down ollama-start ollama-stop health api-test env-setup docker-clean full-clean quick-start status

# Variables
APP_NAME=smart-task-generator
DOCKER_IMAGE=$(APP_NAME):latest
MAVEN=mvn

# Colors for output
GREEN=\033[0;32m
YELLOW=\033[1;33m
RED=\033[0;31m
NC=\033[0m # No Color

## help: Show this help menu
help:
	@echo "$(GREEN)Smart Task Generator - Makefile$(NC)"
	@echo ""
	@echo "$(YELLOW)Available commands:$(NC)"
	@echo ""
	@echo "  $(GREEN)make install$(NC)        - Install Maven dependencies"
	@echo "  $(GREEN)make build$(NC)          - Compile the project"
	@echo "  $(GREEN)make test$(NC)           - Run tests"
	@echo "  $(GREEN)make run$(NC)            - Run the application locally"
	@echo "  $(GREEN)make dev$(NC)            - Start in development mode"
	@echo "  $(GREEN)make clean$(NC)          - Clean build files"
	@echo ""
	@echo "$(YELLOW)Docker Commands:$(NC)"
	@echo "  $(GREEN)make docker-build$(NC)   - Build Docker image"
	@echo "  $(GREEN)make up$(NC)      - Start containers (detached)"
	@echo "  $(GREEN)make down$(NC)    - Stop and remove containers"
	@echo "  $(GREEN)make logs$(NC)    - Show container logs"
	@echo "  $(GREEN)make restart$(NC) - Restart containers"
	@echo ""
	@echo "$(YELLOW)Ollama (Local LLM):$(NC)"
	@echo "  $(GREEN)make ollama-start$(NC)   - Start Ollama locally"
	@echo "  $(GREEN)make ollama-stop$(NC)    - Stop Ollama"
	@echo "  $(GREEN)make ollama-pull$(NC)    - Download llama2 model"
	@echo ""
	@echo "$(YELLOW)Utilities:$(NC)"
	@echo "  $(GREEN)make health$(NC)         - Check application status"
	@echo "  $(GREEN)make api-test$(NC)       - Test API with example"
	@echo "  $(GREEN)make env-setup$(NC)      - Create .env file"
	@echo ""

## install: Install Maven dependencies
install:
	@echo "$(GREEN)📦 Installing dependencies...$(NC)"
	$(MAVEN) clean install -DskipTests

## build: Compile the project
build:
	@echo "$(GREEN)🔨 Compiling the project...$(NC)"
	$(MAVEN) clean package -DskipTests

## test: Run tests
test:
	@echo "$(GREEN)🧪 Running tests...$(NC)"
	$(MAVEN) test

## run: Run the application locally
run:
	@echo "$(GREEN)🚀 Starting application...$(NC)"
	$(MAVEN) spring-boot:run

## dev: Start in development mode
dev:
	@echo "$(GREEN)👨‍💻 Starting in DEV mode...$(NC)"
	@if [ ! -f .env ]; then \
		echo "$(YELLOW)⚠️  .env file not found. Creating...$(NC)"; \
		make env-setup; \
	fi
	@export $$(cat .env | xargs) && SPRING_PROFILES_ACTIVE=dev $(MAVEN) spring-boot:run

## clean: Clean build files
clean:
	@echo "$(GREEN)🧹 Cleaning files...$(NC)"
	$(MAVEN) clean
	@rm -rf target/
	@echo "$(GREEN)✅ Cleanup completed!$(NC)"

## docker-build: Build Docker image
docker-build:
	@echo "$(GREEN)🐳 Building Docker image...$(NC)"
	docker build -t $(DOCKER_IMAGE) .
	@echo "$(GREEN)✅ Image built: $(DOCKER_IMAGE)$(NC)"

## up: Start containers
up:
	@echo "$(GREEN)🐳 Starting containers...$(NC)"
	@if [ ! -f .env ]; then \
		echo "$(YELLOW)⚠️  .env file not found. Creating...$(NC)"; \
		make env-setup; \
	fi
	docker-compose up -d
	@echo "$(GREEN)✅ Containers started!$(NC)"
	@echo "$(YELLOW)📖 Access: http://localhost:8080$(NC)"
	@echo "$(YELLOW)📖 Swagger: http://localhost:8080/swagger-ui.html$(NC)"

## down: Stop and remove containers
down:
	@echo "$(GREEN)🐳 Stopping containers...$(NC)"
	docker-compose down
	@echo "$(GREEN)✅ Containers stopped!$(NC)"

## logs: Show container logs
logs:
	docker-compose logs -f

## restart: Restart containers
restart: down up

## ollama-start: Start Ollama locally
ollama-start:
	@echo "$(GREEN)🤖 Starting Ollama...$(NC)"
	@if command -v ollama >/dev/null 2>&1; then \
		ollama serve & \
		echo "$(GREEN)✅ Ollama started!$(NC)"; \
	else \
		echo "$(RED)❌ Ollama is not installed!$(NC)"; \
		echo "$(YELLOW)Install with: https://ollama.ai/download$(NC)"; \
		exit 1; \
	fi

## ollama-stop: Stop Ollama
ollama-stop:
	@echo "$(GREEN)🤖 Stopping Ollama...$(NC)"
	@pkill -f ollama || echo "$(YELLOW)Ollama is not running$(NC)"

## ollama-pull: Download llama2 model
ollama-pull:
	@echo "$(GREEN)🤖 Downloading llama2 model...$(NC)"
	ollama pull llama2
	@echo "$(GREEN)✅ Model downloaded!$(NC)"

## health: Check application status
health:
	@echo "$(GREEN)🏥 Checking application health...$(NC)"
	@curl -s http://localhost:8080/api/v1/tasks/health || echo "$(RED)❌ Application is not responding$(NC)"

## api-test: Test API with example
api-test:
	@echo "$(GREEN)🧪 Testing API...$(NC)"
	@curl -X POST http://localhost:8080/api/v1/tasks/generate \
		-H "Content-Type: application/json" \
		-d '{ \
			"objective": "Develop a task list application", \
			"maxTasks": 5, \
			"detailLevel": "medium" \
		}' | python3 -m json.tool || echo "$(RED)❌ Error testing API$(NC)"

## env-setup: Create .env file
env-setup:
	@echo "$(GREEN)⚙️  Creating .env file...$(NC)"
	@if [ -f .env ]; then \
		echo "$(YELLOW)⚠️  .env file already exists$(NC)"; \
	else \
		cp env-example.txt .env; \
		echo "$(GREEN)✅ .env file created!$(NC)"; \
		echo "$(YELLOW)⚠️  Configure your API keys in the .env file$(NC)"; \
	fi

## docker-clean: Remove images and containers
docker-clean:
	@echo "$(GREEN)🧹 Cleaning Docker...$(NC)"
	docker-compose down -v
	docker rmi $(DOCKER_IMAGE) || true
	@echo "$(GREEN)✅ Docker cleanup completed!$(NC)"

## full-clean: Complete cleanup (Maven + Docker)
full-clean: clean docker-clean
	@echo "$(GREEN)✅ Complete cleanup done!$(NC)"

## quick-start: Quick start with Docker
quick-start: env-setup docker-build up
	@echo "$(GREEN)✅ Application started!$(NC)"
	@sleep 5
	@make health

## status: Show service status
status:
	@echo "$(GREEN)📊 Service Status:$(NC)"
	@echo ""
	@echo "$(YELLOW)Docker Containers:$(NC)"
	@docker ps --filter "name=$(APP_NAME)" --format "table {{.Names}}\t{{.Status}}\t{{.Ports}}" || echo "$(RED)No containers running$(NC)"
	@echo ""
	@echo "$(YELLOW)Ollama:$(NC)"
	@pgrep -f ollama > /dev/null && echo "$(GREEN)✅ Running$(NC)" || echo "$(RED)❌ Stopped$(NC)"
	@echo ""
	@echo "$(YELLOW)Application:$(NC)"
	@curl -s http://localhost:8080/api/v1/tasks/health > /dev/null && echo "$(GREEN)✅ Running$(NC)" || echo "$(RED)❌ Stopped$(NC)"

