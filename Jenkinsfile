@Library('jenkins-shared-library')_
def helper = new helpers.PipelineHelper(this);

node (label: 'autoSlaveLive') {
  def secrets = [
    [envVariable: 'NOTIFY_API_KEY', name: 'reach-notify-api-key-dev', secretType:'Secret']
  ]

  def URL = "reach-notify"
  def RESOURCE = "SNDCHMINFRGP001-${URL}-${helper.getEnvSuffix()}"
  def APP = "${URL}-${helper.getEnvSuffix()}"
  def AI = "SNDCHMINFRGP001-${helper.getEnvSuffix()}"
  def DATABASE_DB_USER = "SA-AZURE-CHM-SQL-SND@Defra.onmicrosoft.com"
  def KEY_VAULT_NAME = "SECCHMINFKVT001"
  def DATABASE_HOST_NAME = "${helper.getDatabaseServerName()}.database.windows.net"
  def DATABASE_DB_PORT = 1433
  def DATABASE_ADMIN_PASS_NAME_KEY_LOOKUP = "sndSqlDBAdminPass"
  def DB_URL = "jdbc:sqlserver://${DATABASE_HOST_NAME}:${DATABASE_DB_PORT};database=$APP;encrypt=true;schema=dbo;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;authentication=ActiveDirectoryPassword;loginTimeout=30"

  withAzureKeyvault(secrets) {
    def envArray = [
      "APP_NAME=${APP}",
      "SERVICE_NAME='REACH Notify'",
      "URL_PATH=${URL}",
      "ACR_REPO=reach-notify/reach-notify",
      "DB_URL=${DB_URL}",
      "DB_USER=${DATABASE_DB_USER}",
      "DB_PASSWORD=${helper.getKeyVaultPassword(KEY_VAULT_NAME, DATABASE_ADMIN_PASS_NAME_KEY_LOOKUP)}",
      "CONNECTION_STRING=HTTP_REACH_NOTIFY_PORT='8000' WEBSITES_PORT='8000' DB_REACH_NOTIFY_URL='${DB_URL}' DB_USER='${DATABASE_DB_USER}' DB_PASSWORD='${helper.getKeyVaultPassword(KEY_VAULT_NAME, DATABASE_ADMIN_PASS_NAME_KEY_LOOKUP)}' NOTIFY_API_KEY=${NOTIFY_API_KEY} NOTIFY_STATUS_CRON='0 0,30 * * * *' JWT_SECRET_KEY='MySecretKey'",
      "AI_NAME=${AI}",
      "RESOURCE_GROUP=${RESOURCE}",
      "BACKEND_PLAN=SNDCHMINFRGP001-${URL}-${helper.getEnvSuffix()}-service-plan",
      "SET_APP_LOGGING=false",
      "RUN_SONAR=true",
      "PROJECT_REPO_URL=https://giteux.azure.defra.cloud/chemicals/reach-notify.git"
    ]

    withEnv(envArray) {
      def databases = [APP]
      def STORAGE_CONTAINERS = []
      def runIntegrationTests = {
        withMaven(
          options: [artifactsPublisher(disabled: true), jacocoPublisher(disabled: true)], mavenOpts: helper.getMavenOpts()
        ) {
          sh(label: "Run e2e tests", script: "mvn verify -P e2e-tests -DNOTIFY_SERVICE_URL=https://${APP_NAME}.${APPLICATION_URL_SUFFIX}")
        }
      }

      reachPipeline(databases, STORAGE_CONTAINERS, runIntegrationTests)
    }
  }
}
