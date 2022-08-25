# Execution - Test execution from cmd line

To run all tests in an env ./gradlew clean test -Pstaging

To run smoke tests in env ./gradlew clean test -Pstaging -Psmoke  

To run regression tests in env ./gradlew clean test -Pstaging -Pregression

To generate test results report allure serve allure-results
