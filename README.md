#Project 3 - Advanced Task Management Application Extension
##Overview
Project 3 is an advanced extension of the previously developed Task Management Application ("Project2"), now enhancing its capabilities to support multiple users with individually managed task lists. This version introduces server-side data persistence using JSON files, enabling a robust and scalable solution for user and task data management. Developed by Tiago Caniceiro and Daniel Silva, this project further benefits from the frontend development contributions of Selmo Mariano from the preceding project.

##Key Features
User Authentication: Implements a secure login system, ensuring that users can only access their tasks.
Multi-Language Support & Theme Customization: Offers users the ability to personalize their interface language and theme.
Enhanced Task Management: Users can add, delete, and categorize tasks with improved functionalities.
Server-Side Data Persistence: Utilizes JSON files for storing user and task data on the server, ensuring data integrity and availability across sessions.
Drag and Drop Interface: Provides an intuitive way to manage tasks through a drag and drop interface.
Task Visualization: Includes counters and progress indicators for a comprehensive view of tasks.
##Backend Innovations
JSON File Persistence
Data Storage: User and task information is serialized into a JSON format, providing a structured and accessible means of data storage.
Read & Write Operations: The backend implements efficient read and write operations to the JSON file, ensuring data consistency and minimizing I/O overhead.
Scalability: Designed with scalability in mind, allowing for easy expansion of user attributes and task properties without disrupting existing data.
RESTful Web Services
JAX-RS Implementation: Leverages JAX-RS to develop RESTful services, facilitating communication between the frontend and backend.
Comprehensive API Endpoints: Includes endpoints for user registration, login, profile editing, and task management, offering a full suite of web services.
Security: Incorporates security measures for authentication and authorization, safeguarding user data and actions within the application.
##Technologies Used
Frontend: HTML5, CSS3, JavaScript (ES6+)
Backend: JAX-RS, JSON for data persistence
##Tools: Maven for build management, Git for version control, Postman for API testing
##Getting Started
Prerequisites
A modern web browser supporting HTML5, CSS3, and JavaScript ES6+.
Java development environment with Maven and Git.
###Installation and Setup
Clone the project repository:
Copy code
git clone https://github.com/yourproject/Project3.git

##Testing
JavaScript Testing: Implemented using Jest for frontend functionality verification.
API Testing: Utilizes Postman to thoroughly test and validate all RESTful endpoints.
##CLI Support
Detailed instructions on using the CLI for adding random users and tasks, highlighting the RESTful operations utilized.
