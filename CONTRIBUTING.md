# Contributing to AnalyseSI

First off, thank you for considering contributing to AnalyseSI! It's people like you that make AnalyseSI such a great tool.

## Code of Conduct

By participating in this project, you are expected to uphold our Code of Conduct:
- Be respectful and inclusive
- Accept constructive criticism
- Focus on what is best for the community
- Show empathy towards other community members

## How Can I Contribute?

### Reporting Bugs

Before creating bug reports, please check existing issues as you might find out that you don't need to create one. When you are creating a bug report, please include as many details as possible:

- **Use a clear and descriptive title**
- **Describe the exact steps which reproduce the problem**
- **Provide specific examples to demonstrate the steps**
- **Describe the behavior you observed after following the steps**
- **Explain which behavior you expected to see instead and why**
- **Include screenshots if possible**
- **Include your environment details** (OS, Java version, etc.)

### Suggesting Enhancements

Enhancement suggestions are tracked as GitHub issues. When creating an enhancement suggestion, please include:

- **Use a clear and descriptive title**
- **Provide a step-by-step description of the suggested enhancement**
- **Provide specific examples to demonstrate the steps**
- **Describe the current behavior** and **explain which behavior you expected to see instead**
- **Explain why this enhancement would be useful**

### Pull Requests

1. **Fork the repo** and create your branch from `master` or `develop`
2. **Follow the coding style** of the project:
   - Use meaningful variable and method names
   - Add comments for complex logic
   - Follow Java naming conventions
3. **Write tests** for your changes when applicable
4. **Ensure the test suite passes** by running `mvn test`
5. **Update documentation** as needed
6. **Write a good commit message**:
   - Use the present tense ("Add feature" not "Added feature")
   - Use the imperative mood ("Move cursor to..." not "Moves cursor to...")
   - Limit the first line to 72 characters or less

## Development Process

### Setting Up Your Development Environment

1. **Install prerequisites**:
   - Java 17 or higher
   - Maven 3.6 or higher
   - Git
   - Your favorite IDE (IntelliJ IDEA, Eclipse, VS Code)

2. **Fork and clone the repository**:
   ```bash
   git clone https://github.com/your-username/analysesi.git
   cd analysesi
   ```

3. **Build the project**:
   ```bash
   mvn clean install
   ```

4. **Run the application**:
   ```bash
   java -jar target/analyse-si-1.0-SNAPSHOT.jar
   ```

### Project Structure

```
analysesi/
├── src/main/java/org/analyse/
│   ├── core/          # Core functionality
│   ├── merise/        # Merise-specific modules
│   └── main/          # Application entry point
├── src/main/resources/
│   ├── images/        # Icons and images
│   └── langue/        # Internationalization files
└── src/test/java/     # Unit tests
```

### Coding Standards

- **Java Version**: The project uses Java 17
- **Indentation**: Use 4 spaces (no tabs)
- **Line Length**: Maximum 120 characters
- **Comments**: Write in English for code comments
- **Documentation**: Use Javadoc for public methods and classes
- **Logging**: Use `java.util.logging.Logger` instead of `System.out.println`

### Testing

- Write unit tests for new features
- Ensure existing tests pass
- Aim for at least 70% code coverage for new code
- Use JUnit 5 for tests
- Use Mockito for mocking when needed

Run tests with:
```bash
mvn test
```

### Commit Message Guidelines

We follow a simplified version of conventional commits:

- **feat**: A new feature
- **fix**: A bug fix
- **docs**: Documentation only changes
- **style**: Changes that don't affect the meaning of the code
- **refactor**: A code change that neither fixes a bug nor adds a feature
- **perf**: A code change that improves performance
- **test**: Adding missing tests or correcting existing tests
- **chore**: Changes to the build process or auxiliary tools

Example:
```
feat: add export to PostgreSQL support

Add support for exporting MLD to PostgreSQL specific SQL syntax.
Includes proper handling of sequences and array types.

Closes #123
```

## Translation and Internationalization

AnalyseSI supports multiple languages. To contribute translations:

1. Locate the language files in `src/main/resources/langue/`
2. Copy `messages_fr.properties` to `messages_XX.properties` (where XX is your language code)
3. Translate the values (not the keys)
4. Test your translation in the application
5. Submit a pull request

## Community

- **Mailing List**: analysesi@lists.launchpad.net
- **Website**: [http://www.analysesi.com](http://www.analysesi.com)
- **Twitter**: [@analysesi](http://twitter.com/analysesi)

## Recognition

Contributors will be added to the [CONTRIBUTORS.md](CONTRIBUTORS.md) file and recognized in the application's About dialog.

## Questions?

Feel free to open an issue with the `question` label or contact the maintainers directly through the mailing list.

Thank you for contributing to AnalyseSI! 🎉