import jenkins.model.Jenkins
import com.michelin.cio.hudson.plugins.rolestrategy.RoleBasedAuthorizationStrategy
import com.michelin.cio.hudson.plugins.rolestrategy.Role
import com.synopsys.arc.jenkins.plugins.rolestrategy.RoleType
import hudson.security.Permission
import hudson.model.User

Jenkins jenkins = Jenkins.get()
authorizationStrategy=jenkins.getAuthorizationStrategy()
rbas=(RoleBasedAuthorizationStrategy) authorizationStrategy

def users = User.getAll()

ItemRoleMap = rbas.getRoleMaps()[RoleType.Project]
Set<Permission> userPermissions = Permission.getAll().toSet()

if(ItemRoleMap.getRoles().size()!=users.size()){
    println "A new user has signed up. Renew authentication."
    users.each { user ->
    def itemRolePattern = "${user.getId()}-.*"
    def itemRoleName = "${user.getId()}"
    def itemRole = new Role(itemRoleName, itemRolePattern, userPermissions)
    ItemRoleMap.addRole(itemRole)
    ItemRoleMap.assignRole(itemRole,user.getId())
    }
    jenkins.setAuthorizationStrategy(rbas)
}else{
    println "There are no new users."
}
