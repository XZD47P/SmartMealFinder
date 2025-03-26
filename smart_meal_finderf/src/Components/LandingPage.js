import {useMyContext} from "../Store/ContextApi";

const LandingPage = () => {
    const {token} = useMyContext();

    return (
        <h1 className="text-8xl place-self-center">Welcome to Smart Meal Finder!</h1>
    )
}

export default LandingPage;