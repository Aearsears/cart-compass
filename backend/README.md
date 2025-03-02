# backend

## Local Development

### Prerequisites

- Java Development Kit (JDK) 8 or higher
- Apache Maven
- Docker (if using a local DynamoDB instance)

### Running the Application

1. **Compile and package the application:**

    ```bash
    mvn compile && mvn package
    ```

2. **Run the application in developer mode:**

    ```bash
    mvn spring-boot:run
    ```

### Using Docker for Local DynamoDB

If you are using a local DynamoDB instance with Docker, you can start it with the following command:

```bash
docker run -p 8000:8000 amazon/dynamodb-local