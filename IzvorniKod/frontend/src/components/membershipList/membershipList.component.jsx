import React from "react";
import MembershipMetadataContainer from "../membership-metadata-container/membership-metadata-container.component";

class MembershipListComponent extends React.Component {
    constructor(){
        super()
    }

    render() {
        return(
            <div className = 'gymList-container'>
                <div className = 'gymList-header-container'>
                    <h3>Opis</h3>
                    <h3>Trajanje</h3>
                    <h3>Cijena</h3>
                </div>
                {this.props.memberships.map(membership =>
                    <MembershipMetadataContainer id = {membership.id} price = {membership.price} description = {membership.description} interval = {membership.interval} key = {membership.id}/>
                )}
            </div>
        )
    }
}

export default MembershipListComponent