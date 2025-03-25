import {useMyContext} from "../Store/ContextApi";

const LandingPage = () => {
    const {token} = useMyContext();

    return (
        <h1>Welcome to Smart Meal Finder!</h1>
    )
}

export default LandingPage;