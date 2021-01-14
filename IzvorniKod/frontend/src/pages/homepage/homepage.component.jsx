import React from 'react'
import './homepage.styles.scss'
import homePagePicture from '../../assets/silhouette-1975689_1280.svg'

class Homepage extends React.Component{

    render(){
        return(
            <div className = 'homepage-container'>
                <h2>Dobrodošli u WebGym</h2>
                <img src={homePagePicture} height='300px'/>
                <p style={{"font-size":"25px"}}><b>WebGym</b> je aplikacija namijenjena svima željnima organizacije treniranja, bilo da ste trener ili klijent, i upravljanja vlastitim teretanama ako ste voditelj neke teretane.</p>
                <p>Kao klijent možete pregledavati popis svih teretana u aplikaciji i učlaniti se u one koje želite. Također, za svaku teretanu dostupni su vam popisi svih članarina, lokacija i
                    trenera koji su zaposleni njoj. Nadalje, pregledom profila trenera dostupni su vam razni planovi prehrane i treniranja koje možete kupiti, ali i popis svih teretana u kojima odabrani
                    trener radi - to vam omogućava da na temelju nekog dobrog ili lošeg iskustva iz prošle teretane ili vašeg prijatelja znate isplati li se raditi s tim trenerom. Naravno, postoji i stranica
                    na kojoj se nalaze informacije vezane uz vaš profil, a to su kupljeni planovi prehrane ili treninga, sve transakcije koje ste obavili na WebGym aplikaciji te ciljevi koje možete postaviti
                    i voditi evidenciju svojeg napretka u dostizanju istih.</p>
                <p>Kao trener u WebGym aplikaciji možete voditi evidenciju svih transakcija, odnosno vidjeti tko je sve i koje vaše planove prehrane ili treninga kupio. Prilikom pregleda popisa svih
                    svih teretana možete poslati zamolbu za posao u teretane koje vam odgovaraju. Uz to, postoji funkcionalnost koja omogućava pregled popisa svih planova koje nudite gdje možete uređivati već
                    postojeće ili dodavati nove planove prehrane ili treninga. Na kraju, preostaje još mogućnost pregleda vaših klijenata, odnosno onih koji su kupili neki od planova koje nudite.</p>
                <p>Kao voditelj teretane aplikacija nudi sve mogućnosti upravljanja vlastitim teretanama. Dakle, možete vidjeti popis svojih teretana gdje imate mogućnost dodavanja novih i uređivanje
                    postojećih lokacija i dodavanje novih članarina. Isto tako, u aplikaciji možete dodavati nove teretane na istoj stranici gdje se nalazi popis vaših teretana. Uz navedeno, na popisu vaših
                    teretana postoji opcija uklanjanja teretane s popisa, odnosno to znači da prestajete voditi odabranu teretanu. Također, možete pregledati popis svih zahtjeva za rad u vašim teretanama i
                    odgovarati na njih. Nadalje, na stranici popisa svih voditelja u mogućnosti ste odabrati bilo kojeg voditelja i dodati ga u neku od svojih teretana. Naravno, nudi vam se i pregled korisničkog
                    računa i svih vaših transakcija. Osim toga, možete pregledati i popis svih teretana u sustavu kako biste se lakše prilagodili ostalim ponudama.</p>
                <p>Naiđete li na ikakve poteškoće tijekom rada s aplikacijom WebGym, molimo javite nam na e-mail help@webgym.hr. Sretno i uživajte u WebGym-u!</p>
            </div>
        )
    }
}

export default Homepage