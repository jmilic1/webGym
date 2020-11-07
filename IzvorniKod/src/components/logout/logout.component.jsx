import React from 'react'

class Logout extends React.Component{



    componentDidMount(){
        this.props.handleLogout()
        fetch(this.props.backendURL + "logout" , {
            method: 'POST',
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