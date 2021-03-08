# SCATI
Sistema de Controle de Acesso de TI

# Instalação

## Servidor Wildfly
Sistema criado e rodando no Wildfly versão 21.

## Configurações

### Wildfly
Configurações a serem realizadas no servidor Wildfly (pode ser realizado diretamente na interface web de administração

`java:/SCATI`: Configuração para conexão com o banco de dados MariaDB.

`java:jboss/mail/ati`: Configuração para envio de e-mails do sistema.

`java:global/ldap/fab`: Configuração para busca de dados no servidor LDAP.

~~`java:global/ldap/local`: Configuração para busca e atualização do servidor LDAP.~~ Utilizando variáveis de ambiente por causa do LDAP ser SSL e ser necessária uma configuração para aceitar certificados SSL auto assinados.

### Variáveis de ambiente
Para utilizar as variáveis de ambiente basta rodar o comando: `sudo systemctl edit wildfly.service`, e realizar a seguinte configuração:
```
[Service]
Environment="SCATI_DOMAIN=DOMÍNIMO"
Environment="SCATI_LDAP_CONNECTION=CONEXÃO LDAP"
Environment="SCATI_LDAP_USERNAME=NOME DO USUÁRIO DO AD"
Environment="SCATI_LDAP_PASSWORD=SENHA AD"
```

### Configuração inicial do banco de dados
É necessário incluir o primeiro usuário manualmente e incluir os perfis de administração necessários.

# Dúvidas?
Entre em contato!