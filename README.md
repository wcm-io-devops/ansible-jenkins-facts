# wcm_io_devops.jenkins_facts

This role supplies facts about the jenkins instance which are currently:

* Jenkins-Crumb (can be used for CSRF protected requests)
* installed plugins
* update center status

These informations are retrieved by executing a groovy script on the
jenkins instance, see:
[gather_jenkins_facts.groovy](scripts/groovy/gather_jenkins_facts.groovy)

## Requirements

This role requires Ansible 2.4 or higher. On the controlhost the pip package `jmespath` is required.

## Role Variables

Available variables are listed below, along with their default values:

    jenkins_facts_admin_username: admin

Jenkins admin username.

    jenkins_facts_admin_password: admin

Jenkins admin password.

    jenkins_facts_jenkins_hostname: localhost

Hostname of the jenkins instance.

    jenkins_facts_jenkins_port: 8080

HTTP port of the jenkins instance.

    jenkins_facts_jenkins_url_prefix: ""

Url prefix of the jenkins instance, e.g. when running in tomcat.

    jenkins_facts_jenkins_base_url: "http://{{ jenkins_facts_jenkins_hostname }}:{{ jenkins_facts_jenkins_port }}{{ jenkins_facts_jenkins_url_prefix }}"

The base url of the jenkins instance.

    jenkins_facts_script_timeout: 30

The timeout for gathering facts via jenkins_script.

    jenkins_facts_uri_timeout: 30

The timeout for uri commands.

## Dependencies

This role has no hard dependencies but interacts heavily with the

* [jenkins-plugins](https://github.com/wcm-io-devops/ansible-jenkins-plugins.git)

role(s).

## Facts

The following facts are supplied by the `jenkins_facts` role:

* `{{ jenkins_facts_updatecenter }}`
  * Timestamp of last update `updateTimestamp`
  * Seconds since last update `updateAgeSeconds`
  * Current timestamp `currentTimestamp`
* `{{ jenkins_facts_plugins_installed }}`: List of all installed plugins
  with their version, their status and if the plugin has an update
* `{{ jenkins_facts_plugins_failed }}`: List of plugins that failed to
  load with the cause
* `{{ jenkins_facts_csrf_enabled }}`: Status if CSRF protection is
  enabled or disabled (in newer Jenkins instances CSRF protection is mandatory)
* `{{ jenkins_facts_csrf_crumb }}`: Fresh/current CSRD Jenkins-Crumb token
* `{{ jenkins_facts_cookie }}`: Fresh/current Cookie token

## Example Playbook

This playbook gathers facts from a jenkins instance and outputs them:

    - hosts: jenkins
      roles:
        - { role: wcm_io_devops.jenkins_facts }

## License

Apache 2.0
