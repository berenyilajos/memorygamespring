# memorygamespring
## Webes Memória játék
### Elemek, amiket tartalmaz: Spring, JPA, Repository-k, 2 adatbáziskapcsolat ennek megfelelő tranzakciókezeléssel, exception kezelés (a REST részhez, ami az eredmények mentése), stb.

Rendszerfeltételek:

A program 11-es Javával futtatható. Maven: 3.6.0 vagy  újabb

Adatbázis:
Oracle 10 XE (de 11-es adatbázis is megteszi)

A projektben levő com.oracle.ojdbc6 dependency-ről:
Ez local maven repóból való, vagyis a fő projektben található (a memorygamespring mappában).
A memorygame-dao projekt pom.xml-ben van hozzáadva a projecthez.

Először hozzuk létre az MG user/schema-t (jelszó 'mg'), a createMGuser.sql segítségével.
Hozzuk létre a táblákat a usersResultCreate.sql futtatásával.
Ez pl. SQLDeveloperben egyben lefuttatható,
de pl. az Oracle 10 Xe böngészős adatbáziskezelőjében (apex) a "/" jelekkel elválasztott részeket egyenként kell futtatni.
Utána az alapadatokat a csv (először a users, majd a result) file-okból importáljuk,
vagy a usereket regisztráció útján is létre lehet hozni.

Majd hozzuk létre az MGD user/schema-t (jelszó 'mgd'), a createMGDuser.sql segítségével.
Hozzuk létre a ResultData táblát a resultDataCreate.sql futtatásával.

A memorygamespring mappában futtasuk az "mvn install" parancsot.

Ezután a memorygame/target mappából futtatható a java -jar memorygame.jar paranccsal.

Ezután az oldal elérhető a localhost:8484/memorygame url-en.

Wadl file információk: localhost:8484/memorygame/game/application.wadl
(Ez - a springben nem találtam megfelelő wadl generátort - nem működik tökéletesen)

Jó játékot!
