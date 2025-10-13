import {useState} from "react";
import {Box, MenuItem, Modal, TextField, Typography} from "@mui/material";
import Buttons from "./Buttons";

const FoodEntryQuantityModal = ({open, onClose, onConfirm, itemName}) => {
    const [quantity, setQuantity] = useState("");
    const [unit, setUnit] = useState("grams");

    const handleConfirm = () => {
        if (!quantity || quantity <= 0) {
            return;
        }
        onConfirm({quantity: parseFloat(quantity), unit});
        setQuantity("");
        setUnit("grams");
    };

    return (
        <Modal open={open} onClose={onClose}>
            <Box
                sx={{
                    position: "absolute",
                    top: "50%",
                    left: "50%",
                    transform: "translate(-50%, -50%)",
                    bgcolor: "background.paper",
                    p: {xs: 2, sm: 3, md: 4},
                    borderRadius: 2,
                    boxShadow: 24,
                    width: "60%",
                }}
            >
                <Typography variant="h6" gutterBottom>
                    Add quantity for {itemName}
                </Typography>

                <Box display="flex" gap={2} alignItems="center" mb={2}>
                    <TextField
                        label="Quantity"
                        type="number"
                        value={quantity}
                        onChange={(e) => setQuantity(e.target.value)}
                        size="small"
                        sx={{width: "60%"}}
                    />
                    <TextField
                        select
                        label="Unit"
                        value={unit}
                        onChange={(e) => setUnit(e.target.value)}
                        size="small"
                        sx={{width: "40%"}}
                    >
                        <MenuItem value="grams">grams</MenuItem>
                        <MenuItem value="pieces">pieces</MenuItem>
                    </TextField>
                </Box>

                <Box display="flex" justifyContent="flex-end" gap={1}>
                    <Buttons
                        type="button"
                        onClickhandler={onClose}
                        className="bg-gray-300 text-black px-3 py-1 rounded hover:bg-gray-400"
                    >
                        Cancel
                    </Buttons>
                    <Buttons
                        type="button"
                        disabled={!quantity}
                        onClickhandler={handleConfirm}
                        className="bg-green-500 text-white px-3 py-1 rounded hover:bg-green-600"
                    >
                        Add
                    </Buttons>
                </Box>
            </Box>
        </Modal>
    );
};

export default FoodEntryQuantityModal;
