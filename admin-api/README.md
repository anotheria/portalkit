Guide to set up and run the Portalkit Admin-API.

Portalkit Admin is a docker container, so to run it in a few steps, Docker Engine should be installed. Otherwise, build the artifact and run it in tomcat manually.

To run a Portalkit Admin use:<br>
``docker run -d -p<out_port>:8080 -p<out_rmi_port>:9299 -v <path_to_config_dir>/pk-admin-api-configuration:/app/tomcat/pk-admin-api-configuration -v <path_to_cms_content_dir>/cms-content:/app/cms-content --add-host dime.registry.host:<distribute_me_ip> anotheria/pk-admin-api:latest``
- out_port: port that is used to reach the API outside the container.
- rmi_port: rmi_port to connect to API via RMI (Moskito).
- path_to_config_dir: path to directory where configs are stored (pk-admin-api-config.json, pk-admin-api-authentication-config.json etc).
- path_to_cms_content_dir: path to directory where cms-content is stored.
- distribute_me_ip: ip to DistributeMe registry.
