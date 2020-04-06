# Consent Service


Information systems deal with data that belong to natural persons. Usually, such
data is exchanged and processed because of the requirements from the law, and
therefore, there is no need for explicit permission from the owner of the data.

Increasingly, the use cases that improve user experience somewhere or enable some
academic or commercial advancement are found. These use cases require the owner
of the data to express explicit consent that their data may be processed for
this particular purpose.

For most of the information systems involved, the topic of consent management is
quite similar to the topic of authentication – it has a lot of complicated
details that are not directly related to the domain of the information system in
question. Most of those details can be delegated to some other system that only
returns the information of the presence and the content of the consent to the
system that needs the consent to perform some operation with the data – either
return it to some third party or use it in a novel way.

For the owners of the data having multiple systems that manage their consents
creates the problem of having clear overview of which consents they have given
and how to manage (withdraw) them, if needed.

The shared Consent Service should:

- Offer a unified user interface that allows the owner of the data to have a
  clear overview of which consents it has given, when do these expire and if
  there has been any operations performed (data exchanged) using those consents

- Offer a unified API for depending information systems to check if there is a
  valid consent to process some specific data

- Track the actual usage of consents


Service Provider
-----


A Service Provider is an organization (a legal entity) that offers a Protected
Service. A Service Provider has to be a member of the X-Road and is identified
by its X-Road identifier.

A Service Provider may also have attributes (name, registry number, contacts of
data protection officer) that must be available to the Data Subject before
giving a Consent and which may end up in the actual signed document (if
applicable), but which are not part of the Consent Service protocols. That data
reaches the system of the Consent Service by other means (from the trade
registry, from the agreement of the use of the Consent Service or from somewhere
else), if it needs to use it.


Service Declaration
-----

- Identifier in the scope of the Service Provider, issued by the Service
  Provider

- Name in human language

- Description of the service in human language, including the description of
  Protected Data and processing involved:

  - What does the Protected Service use as input parameters

  - What data is processed (used to produce the response for the Client even if
    not directly returned)

  - What data (and for which time period, if applicable) is returned

- End of validity. The moment until which this declaration is valid and the
  Service Provider still offers the Protected Service as declared in this
  declaration. Validity can not be extended, only shortened. A Consent can only
  be valid as long as all the Service Declarations used in the corresponding
  Purpose Declaration are valid.

- Maximal allowed cache time for the validation response for that service. For
  cases where online validation of every request is not possible. Also means
  that revocation of the Consent may take effect after that time has passed and
  therefore must be clearly communicated to the Data Subject

The exact technical meaning of the service description (that is: what exactly
happens in the system of the Service Provider) is defined by the Service
Provider. It is up to the Service Provider to guarantee that what is declared
(described) and what actually happens are the same.

Once declared, the Service Provider cannot change the declaration, if there is a
need for an update, it should submit a new Service Declaration. The Service
Provider can invalidate a Service Declaration. It is assumed that for updated
services the old and new Service Declaration can be valid at the same time and
the Service Provider should be able to understand which version of the service
it is allowed to provide by the Service Declaration identifier bound to the
Consent in use in any request.


