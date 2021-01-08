import React from "react";

class LocationListComponent extends React.Component {
    constructor(){
        super()
    }

    render() {
        return(
            <div></div>
            // <div className = 'gymList-container'>
            //     <div className = 'gymList-header-container'>
            //         <h3>Opis</h3>
            //         <h3>Cijena</h3>
            //         <h3>Trajanje</h3>
            //         <h3>Detaljnije</h3>
            //     </div>
            //     {this.props.memberships.map(membership =>
            //         <LocationMetadataContainer id = {membership.id} price = {membership.price} description = {membership.description} interval = {membership.interval} key = {membership.id}/>
            //     )}
            // </div>
        )
    }
}

export default LocationListComponent