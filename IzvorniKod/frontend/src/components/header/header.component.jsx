import React from 'react'
import './header.styles.scss'
import { Link } from 'react-router-dom'
import { ReactComponent as Logo } from '../../assets/logo.svg'

class Header extends React.Component {

    render() {
        return (
            <div className='header-container'>
                <Link className='logo-container' to='/'>
                    <Logo className='logo' />
                </Link>
                <div className='link-container'>
                    {this.props.role === "COACH" &&
                        <Link to='/myClients'>Moji klijenti</Link>
                    }
                    {this.props.role === "COACH" &&
                        <Link to='/myPlans'>Moji planovi</Link>
                    }

                    {this.props.role === "OWNER" &&
                        <Link to='/jobRequests'>Zahtjevi za posao</Link>
                    }

                    {this.props.role === "OWNER" &&
                        <Link to='/myGyms'>Moje teretane </Link>
                    }

                    {this.props.role === "ADMIN" &&
                        <Link to='/allTransactions'>Sve transakcije</Link>
                    }

                    <Link to='/gymList'>Popis teretana </Link>

                    {this.props.role === "ADMIN" &&
                        <Link to='/userList'>Popis korisnika</Link>
                    }

                    {this.props.role === "OWNER" &&
                        <Link to='/userList'>Popis voditelja</Link>
                    }

                    {this.props.loggedIn &&
                        <Link to='/myProfile'>Moj profil</Link>
                    }

                    {this.props.loggedIn ?
                        <Link to='/logOut'>Odjava</Link>
                        :
                        <Link to='/auth'>Prijava</Link>

                    }

                </div>
            </div>
        )
    }
}


export default Header
