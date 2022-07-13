# REACH Notify

A Gov Notify proxy for REACH services

## Setup

### Java

This application runs on Java 11.

### Environment variables

```bash
export NOTIFY_API_KEY={{Notify api key}}
export DB_REACH_NOTIFY_URL="jdbc:sqlserver://localhost:1433;database=notify;"
export HTTP_REACH_NOTIFY_PORT=8080
export APPLICATION_INSIGHTS_IKEY=A
export NOTIFY_STATUS_CRON="0 0,30 * * * *"
```

### Database

The application expects a SQL Server database named `notify` running on the ${DB_REACH_NOTIFY_URL}

#### Liquibase

To run the latest Liquibase changesets against the DB, from the `database` directory run

```
mvn process-resources
```

## Endpoints

**/healthcheck**

`GET`

Returns `ok` if service is up and running.

**/email**

*Requires Authorization header*

`POST`

Sends an email to the specified email. Content is based on the event which gets translated to a Notify template id.

**Example request body**
```json
{
	"event": "NEW_REACH_MESSAGE",
	"emailAddress": "test@example.com",
	"personalisation": {
		"first_name": "John",
		"last_name": "Smith",
		"submission_name": "Submission",
		"legal_entity": "Company Inc."
	}
}
```


