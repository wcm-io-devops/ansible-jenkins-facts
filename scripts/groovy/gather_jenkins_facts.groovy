import hudson.model.UpdateCenter
import hudson.PluginManager
import hudson.PluginManager.FailedPlugin
import hudson.model.UpdateSite.*
import groovy.json.JsonOutput
import hudson.security.csrf.*

//get the current update center status
UpdateCenter center = Jenkins.instance.updateCenter
lastUpdated = center.getLastUpdatedString()
List<UpdateSite> updateSiteList = center.getSiteList()

long updateTimestamp = 0;
for (UpdateSite s : updateSiteList) {
  if (s.getDataTimestamp()>updateTimestamp) {
    updateTimestamp = s.getDataTimestamp();
  }
}
currentTimestamp = new Date().getTime()
updateAgeSeconds = (currentTimestamp - updateTimestamp).intdiv(1000)

// get pluginmanager status
PluginManager pluginManager = Jenkins.instance.pluginManager
// get all plugins
List<PluginWrapper> installedPlugins = pluginManager.getPlugins()
List<FailedPlugin> failedPlugins = pluginManager.getFailedPlugins()
//println(allPlugins)

/**
 * Transform installed plugin info into map
 */
Map getPluginInfo(List<PluginWrapper> plugins) {
  Map ret = [:]
  for (PluginWrapper plugin in plugins) {
    UpdateSite.Plugin updateInfo = plugin.getUpdateInfo()
    Map tmpPlugin = [
        version: plugin.getVersion(),
        enabled: plugin.isEnabled(),
        active: plugin.isActive(),
        hasUpdate: updateInfo != null
    ]
    ret[plugin.getShortName()] = tmpPlugin
    //println("${plugin.getShortName()} : ${tmpPlugin}, ${updateInfo}")
  }
  return ret
}
/**
 * Transform failed plugin info into map
 */
Map getFailedPluginInfo(List<FailedPlugin> failedPlugins) {
  Map ret = [:]
  for (FailedPlugin failedPlugin in failedPlugins) {
    ret[failedPlugin.name] = [
        cause: failedPlugin.cause.message
    ]
  }
  return ret
}

Map installedPluginsTransformed = getPluginInfo(installedPlugins)
Map failedPluginsTransformed = getFailedPluginInfo(failedPlugins)

//println("failedPluginsTransformed: ${failedPluginsTransformed}")

// get information about crumbissuer (CSRF)
CrumbIssuer crumbIssuer = Jenkins.instance.getCrumbIssuer()

def json = JsonOutput.toJson([
    updatecenter: [
        status: [
            updateTimestamp: updateTimestamp,
            updateAgeSeconds: updateAgeSeconds,
            currentTimestamp: currentTimestamp,
        ]
    ],
    pluginmanager: [
        installedPlugins: installedPluginsTransformed,
        failedPlugins: failedPluginsTransformed
    ],
    instance: [
        csrf : [
            enabled : crumbIssuer != null
        ]
    ]
])

return json.toString()