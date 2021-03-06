# slack-sonar-notifier #

Plugin do Maven para notificar possíveis problemas relatados no Sonarqube em um canal do Slack (https://slack.com/)

## Usando o plugin
### 1. Crie um _webhook_ no slack
Crie um *Incoming WebHooks* no seu canal do Slack

Acesse a URL: _https://(seu-canal).slack.com/apps/manage/custom-integrations_

### 2. Adicione o plugin do pom.xml

```xml
<build>
    <plugins>
        <plugin>
            <groupId>br.com.gsw.slack</groupId>
            <artifactId>sonar-notifier</artifactId>
            <version>1.0-SNAPSHOT</version>
            <configuration>
                <skipNotifier>false</skipNotifier>
                <breakNotifier>false</breakNotifier>
                <sonar>
                    <key>${project.groupId}:${project.artifactId}</key>
                    <url>http://sonarqube.gsw.com.br</url>
                    <login>sonaruser</login>
                    <password>sonarpass</password>
                    <coverage>60.0</coverage>
                </sonar>
                <scm>
                    <url>https://github.com/gswteam/slack-sonar-notifier</url>
                    <branch>origin/master</branch>
                    <user>fabiohbarbosa</user>
                    <commit>caaef090d185e90132043487bee78b877455e4a1</commit>
                </scm>
                <slack>
                    <webhook>https://hooks.slack.com/services/ASHDIU98/98173JOIJ/sv9RRmWpvTes2Oc3y5QeY54G</webhook>
                    <onlyErrors>true</onlyErrors>
                </slack>
            </configuration>
        </plugin>
    </plugins>
</build>

```

***

**skipNotifier**: 
Não executa o plugin
* _Type_: Boolean
* _Property_: -DskipNotifier
* _Default_: false

***

**breakNotifier**: 
Caso seja encontrado erros no sonar, quebra o build
* _Type_: Boolean
* _Property_: -DbreakNotifier
* _Default_: false

***

**sonar.key***:
Chave do projeto no sonar, podendo utilizar o `project.groupId:project.artifactId`, ou o id no sonar (entre no projeto no sonar e verifique o ID na url).
* _Type_: String
* _Property_: -Dsonar.key
* _Default_: ${project.groupId}:${project.artifactId}

***

**sonar.url***:
URL do sonar
* _Type_: String
* _Property_: -Dsonar.url ou -Dsonar.host.url

***

**sonar.login**:
Login para acesso ao sonar
* _Type_: String
* _Property_: -Dsonar.login

***

**sonar.password**: 
Senha do usuário para acesso ao sonar
* _Type_: String
* _Property_: -Dsonar.password

***

**sonar.coverage**:
Mínino de cobertura de testes exigida no sonar

_Só tem utilidade quando as properties `breakNotifier` ou `slack.onlyErrors` estiverem habilitadas_
* _Type_: Double
* _Property_: -Dsonar.coverage

***

**scm.url**: 
URL do repositório (SVN/GIT)
* _Type_: String
* _Property_: -Dscm.url

***

**scm.branch**: 
Branch no repositório (SVN/GIT)
* _Type_: String
* _Property_: -Dscm.branch

***

**scm.user**:
Usuário no repositório (SVN/GIT)
* _Type_: String
* _Property_: -Dscm.user

***

**scm.commit**: 
Commit do usuário no repositório
* _Type_: String
* _Property_: -Dscm.commit

***

**slack.webhook***: 
URL de webhook do slack
* _Type_: String
* _Property_: -Dslack.webhook

***


**slack.onlyErrors**: 
Só notifica os erros no slack
* _Type_: Boolean
* _Property_: -Dslack.onlyErrors
* _Default_: true

***

> \* Obrigatórios

### 3. Execute o plugin
```sh
mvn sonar-notifier:sonar-notifier
```

## Mais Informações sobre o plugin
```sh
mvn help:describe -Dplugin=sonar-notifier -Ddetail
```

---

## Compilando o plugin

### Install
#### Testes Unitários e Integrados
Os testes integrados dependem de um Sonar e um Slack configurados para serem executados.

Basta passar os dados de configurações como variáveis para a execução dos testes.

Exemplo:
```sh
mvn clean install \
-Dsonar.key=br.com.gsw.slack:sonar-notifier \
-Dsonar.host.url=http://sonarqube.gsw.com.br \
-Dsonar.coverage=60.0 \
-Dslack.webhook=https://hooks.slack.com/services/ASHDIU98/98173JOIJ/sv9RRmWpvTes2Oc3y5QeY54G
```

#### Somente Testes Unitários
```sh
mvn clean install -DskipITs
```
