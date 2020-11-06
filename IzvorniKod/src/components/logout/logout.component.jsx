import React from 'react'

class Logout extends React.Component{



    componentDidMount(){
        this.props.handleLogout()
        fetch('http://localhost:5000/logout', {
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