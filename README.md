Analisian zuhaitzaren lehenengo erregelara laburtzen dugunean, behin jadanik lortu dugula erdibideko programa (hau pantailan bistaratzen da eta egikaritu egiten da, ondo dagoela froga moduan), hurrengo pausoa izango da makina-lengoaiako programa lortzea. Aurretik erdibideko programa oinarrizko blokeetan banatuko dugu. Hauetan instrukzioak segidan joango dira, saltorik gabe, azkenengora heldu arte. Gainera blokeetako instrukzio-lehena, programa osoaren lehenengo instrukzioa edo salto baten helmuga, izango da. Gordeko dira tbloques datu-egituran.
Hau dena egiten da ejecutarAccionSemantica metodo barruan, case 1 kasuan (gramatikako lehenengo erregela laburtzerakoan). 
Oinarrizko blokeak lortzearekin batera, aldagai bakoitzarentzako kalkulatzen da hurrengo erabileraren datuak eta begiztak bilatzen dira blokeen artean. Informazio hori guztia erabilgarria izango da hurrengo itzulketa fasean, erregistroak aukeratzerakoan, zeren dakigunez makinako instrukzioak beti izango dira azkarragoak erregistroak erabiltzen badituzte operadore moduan. Eta begiztetan zehar erabiltzen diren aldagaiak erregistro jakinen betean gordetzen badira, aurreztu egingo da exekuzioan.
Kontsolan deitzen dugunean exekutatzeko gure programa (parser_kpa) parametro gisa pasatuko diogu lehendabizi algoritmoa idatzita dagoen fitxategia, eta gero zeren makina-lengoaietan nahi dugun itzulketa prozesua (TXORI,8086,...). Hasieran TXORI mihiztadura-hizkuntza izango da aukera bakarra.