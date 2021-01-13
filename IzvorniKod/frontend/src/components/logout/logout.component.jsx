import React from 'react'

class Logout extends React.Component{



    componentDidMount(){
        this.props.handleLogout()
        fetch(this.props.backendURL + "logOut" , {
            method: 'GET',
            credentials: 'include'
        })
        this.props.history.push('/')
    }

    render(){
        return(
            <div></div>
        )
    }
}

export default Logout