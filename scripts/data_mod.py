import pandas as pd
import sys

def modify_data_csv(input_name, output_name):
    data_frame = pd.read_csv(input_name, header=0)
    new_data = data_frame[["Pytanie [ENG]","Odpowiedź A [ENG]", "Odpowiedź B [ENG]", "Odpowiedź C [ENG]", "Poprawna odp", "Media", "Kategorie"]].copy()
    new_data.rename(columns={'Pytanie [ENG]': 'question', 'Odpowiedź A [ENG]': 'answer_a', 'Odpowiedź B [ENG]': 'answer_b', 'Odpowiedź C [ENG]': 'answer_c', 'Poprawna odp': 'answer_correct','Media': 'media', 'Kategorie':'category'}, inplace=True)
    new_data.to_csv(output_name, index=False)

def main():
    if len(sys.argv) < 2:
        print("Provide input file path and output file path as arguments ...")
    return

    inp = sys.argv[1]
    out = sys.argv[2]
    modify_data_csv(inp, out)


if __name__ =="__main__":
    main()