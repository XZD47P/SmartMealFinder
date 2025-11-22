import {Box, Modal, Typography} from "@mui/material";
import Buttons from "./Buttons";

const ConfirmModal = ({open, onConfirm, onCancel}) => {
    return (
        <Modal open={open} onClose={onCancel}>
            <Box
                sx={{
                    position: "absolute",
                    top: "50%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    bgcolor: "background.paper",
                    p: 4,
                    borderRadius: 2,
                    boxShadow: 24,
                    width: 350,
                }}
            >
                <Typography variant="h6" gutterBottom>
                    Disable Profiling?
                </Typography>

                <Typography variant="body2" sx={{mb: 3}}>
                    Are you sure you want to opt-out of profiling?
                    You will lose all the recommendation functionality!
                </Typography>

                <Box sx={{display: "flex", justifyContent: "flex-end", gap: 2}}>
                    <Buttons
                        type="button"
                        className="border border-gray-400 text-gray-700 px-4 py-2 rounded hover:bg-gray-100"
                        onClickhandler={onCancel}
                    >
                        Cancel
                    </Buttons>

                    <Buttons
                        type="button"
                        className="bg-red-600 text-white px-4 py-2 rounded hover:bg-red-700"
                        onClickhandler={() => onConfirm()}
                    >
                        Yes, disable
                    </Buttons>
                </Box>
            </Box>
        </Modal>
    );
};

export default ConfirmModal;