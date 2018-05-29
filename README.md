# jenkins-facts

This role supplies facts about the jenkins instance which are currently:

* Jenkins-Crumb (can be used for CSRF protected requests)
* installed plugins
* update center status

These informations are retrieved by executing a groovy script on the
jenkins instance, see:
[gather_jenkins_facts.groovy](scripts/groovy/gather_jenkins_facts.groovy)

## Requirements

This role requires Ansible 2.4 or higher and works with AEM 6.1 or higher. The role requires an AEM service that can be controlled with the Ansible `service` module to be installed on the target machine.

## Role Variables

Available variables are listed below, along with their default values:

    jenkins_facts_admin_username: admin

Jenkins admin username

    jenkins_facts_admin_password: admin

Jenkins admin password

    jenkins_facts_jenkins_hostname: localhost

Hostname of the jenkins instance

    jenkins_facts_jenkins_port: 8080

HTTP port of the jenkins instance

    jenkins_facts_jenkins_url_prefix: ""

Url prefix of the jenkins instance, e.g. when running in tomcat

    jenkins_facts_jenkins_base_url: "http://{{ jenkins_facts_jenkins_hostname }}:{{ jenkins_facts_jenkins_port }}{{ jenkins_facts_jenkins_url_prefix }}"

The base url of the jenkins instance

    jenkins_facts_debug: false

When set to enable the role will log some debug information

## Facts

The following facts are supplied by the `jenkins-facts` rolen:

* `{{ jenkins_facts_updatecenter }}`
  * Timestamp of last update `updateTimestamp`
  * Seconds since last update `updateAgeSeconds`
  * Current timestamp `currentTimestamp`
* `{{ jenkins_facts_plugins_installed }}`: List of all installed plugins
  with their version, their status and if the plugin has an update
* `{{ jenkins_facts_plugins_failed }}`: List of plugins that failed to
  load with the cause
* `{{ jenkins_facts_csrf_enabled }}`: Status if CSRF protection is
  enabled or disabled
* `{{ jenkins_facts_csrf_crumb }}`: Fresh/current CSRD Jenkins-Crumb token

## Example Playbook

This playbook gathers facts from a jenkins instance and outputs them:

    - hosts: jenkins
      roles:
        - { role: jenkins-facts, jenkins_facts_debug: true }

## License

Apache 2.0
