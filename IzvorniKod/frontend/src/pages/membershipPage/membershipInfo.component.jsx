import React from "react";
import './membershipInfo.styles.scss'

class MembershipInfo extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            membership: null
        }
    }

    async componentDidMount() {
        const url = this.props.backendURL + "membership";


        const params = {id: this.props.match.params.id};

        await fetch(url, {
            method: "GET",
            headers: { 'Content-Type': 'application/json' },
            credentials: 'include',
            body: JSON.stringify({id: params.id})
        }).then(res => {
            if (!res.ok) {
                throw new Error("HTTP Error! " + res.status)
            } else {
                return res.json()
            }
        }).then(membership => {
                this.setState({
                    membership: membership
                })
            }
        ).catch(e => {
            alert("Došlo je do pogreške: " + e.message)
        })
        console.log(this.state.membership)
    }


    render() {
        return (
           <div></div>
        );
    }
}

export default MembershipInfo