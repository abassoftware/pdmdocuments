
pdmDocuments

Documentation


PDMDocuments ist ein Infosystem, welches über eine Rest-Api auf das PDM-System zugreift und die mit dem Artikel verknüpften Dokumente abholt und anzeigt. Das Infosystem ist durch ein Layout in den Druckprozess eingebunden und druckt zu einem Beleg die freigegeben Zeichnungen.

Derzeit ist der Zugriff auf die folgenden PDM-System implementiert :

•Keytech

•ProFile

•SolidWorksPDM (Lösung von dem SolidWorks-Partner Coffee)


Technische Vorraussetzungen

abas: Version 2017 / 2018


Keytech: Es muss die Keytech-RestAPI auf dem Server von Keytech installiert sein.


Für den Zugriff benötigen wir einen Benutzer in Keytech, welcher nur Zugriffsrechte für die Dokumente hat, welche in PDMDocuments angeschaut werden sollen.


ProFile:


Es muss der ProFile APP Server installiert sein.


Für den Zugriff benötigen wir einen Benutzer in ProFile, welcher nur Zugriffsrechte für die Dokumente hat, welche in PDMDocuments angeschaut werden sollen.


SolidWorks: Die RestAPI von Coffee muss installiert sein.


Installation



PDMDocuments wird mit dem abas-esdk-Installer installiert.


pdm-<Version>.jar


Es wird das Infosystem PDMDOCUMENTS, das Layout PDMDOCUMENTS, Aufrufparameter und die entsprechenden Programme installiert.


Nacharbeiten


Die Aufrufparameter zum Aufrufen des Infosystem aus den Masken:


Maske 2 Artikel : PDMDOCUMENTSTEIL Maske 42 Bestellung : PDMDOCUMENTSEK Infosystem APAPIER : PDMDOCUMENTSAPAPIER Infosystem PRODLIST : PDMDOCUMENTSPRODLIST


Einbinden des Layouts PDMDOCUMENT in die individuellen Drucklayouts.


Anpassungen



Über den Berichtsfuss(bfuss) kann ein FOP eingefügt werden.


Konfiguration




ProFile



Folgende Werte müssen bei dem PDM-System ProFile konfiguriert werden:






Feld

Bedeutung

Beispiel-Eintrag



PDM-Server
 
Der Aufruf der Rest-Api von ProFile
 
<ProFile-Server>
 

Benutzer
 
Der ProFile-Benutzer mit dem die Dokumente gesucht werden soll
  

Passwort
 
Das Passwort des ProFile-Benutzer
  

SQLServer IP / DNS-Name
 
SQL-Server
  

SQLServer Port
 
Zugriffsport auf den SQL-Server
 
z.B. 1433
 

SQLServer Benutzer
 
Benutzer für Zugriff auf SQL-Server
  

SQLServer Passwort
 
Passwort für den Benutzer für Zugriff auf SQL-Server
  

SQLServer Datenbank
 
Profile Datenbank im SQL-Server
  

Tenant (Datenbank Profile)
 
Über den Tenant wird auf die ProFile-RestApi zugegriffen
 
z.B.: profile
 

SqlServerDriver
 
Java SQL ServerDriver
 
com.microsoft.sqlserver.jdbc.SQLServerDriver
 

Feldname für AbasNumber
 
Feld für die Abas - Identnummer in der Profile Artikeldatenbank
 
z.B.: /Part/pdmPartItemNumber
 

Feldname für PartID
 
Feld für die ID des Artikel in der Artikeldatenbank in Profile
 
z.B.: /Part/pdmPartID
 

Feldname für orgName
 
Feld für den Original-Dateinamen in der Dokumentendatenbank in ProFile
 
z.B.: /Document/orgName
 

Feldname für docVersionBaseID
 
Feld für die original DocID in der Dokumentendatenbank in ProFile
 
z.B.: /Document/docVersionBaseId
 

Feldname für docType
 
Feld für den Dokumententyp in der Dokumentendatenbank in ProFile
 
z.B.: /Document/docType

Diese Felder sind für den Zugriff auf ProFile notwendig.
 


Keytech


Folgende Werte müssen bei dem PDM-System Keytech konfiguriert werden:






Feld

Bedeutung

Beispiel-Eintrag



PDM-Server
 
Der Aufruf der Rest-Api von Keytech
 
http://<keytechserver>:8086/keytech
 

Benutzer
 
Der Keytech-Benutzer mit dem die Dokumente gesucht werden soll
  

Passwort
 
Das Passwort des Keytechbenutzer
  


ProFile



Folgende Werte müssen bei dem PDM-System SolidWorksPDM von Coffee konfiguriert werden:






Feld

Bedeutung

Beispiel-Eintrag



PDM-Server
 
Der Aufruf der Rest-Api von Coffee
 
Eintrag: <Coffee-Server>:85
 


Additional Page



Another page with content!


Partner



Unser PDM-Partner :


http://www.procad.de


http://www.keytech.de


http://www.coffee.de


About



pdmDocuments
© abas Software GmbH


https://www.abas-erp.com/
