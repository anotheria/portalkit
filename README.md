[![Maven Central](https://maven-badges.herokuapp.com/maven-central/net.anotheria.portalkit/portalkit/badge.svg)](https://maven-badges.herokuapp.com/maven-central/net.anotheria.portalkit/portalkit)
[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)


PortalKIT.
=========

PortalKIT is a collection of services and apis for use across different projects.

It consists of following services:

1. AccountService - managing of accounts.
2. AccountArchiveService - managing of archived accounts, used to store emails and ids permanently for deleted accounts. Mainly to reduce garbage in AccountService.
3. AccountListService - managing of account lists, used for favorite lists and other use-cases where a relation one to many is required.
4. AccountSettingsService - management of schema-less data (key value pairs) in context of an account. This is a PortalKIT specific implementation of Dataspace service from ano-prise. Whenever some key/value data is stored we can start by using the AccountSettings without having to setup a specific db. Should always be used in context of an account, i.e. when a logged in Account performed some action.
5. AuthenticationService - management of credentials for user login (password) and tool login (tokens) (mails, cookie login etc). Its separated on purpose from the AccountService to reduce risk of data-theft. 
6. ForeignIdService - management of foreign ids for 3rd party systems. I.e. storage of facebook login token and similar.

to be continued.
