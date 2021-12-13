use std::fs::File;
use std::io::Read;

pub fn day1() {
    for filename in ["input/day1_example1.txt", "input/day1.txt"] {
        part_one(filename);
    }

    for filename in ["input/day1_example1.txt", "input/day1.txt"] {
        part_two(filename);
    }
}

fn part_one(filename: &str) {
    let nums = load_ints(filename);
    println!("{}", count_increasing(nums))
}

fn part_two(filename: &str) {
    let nums = load_ints(filename);
    println!("{}", count_increasing_sums(nums))
}

fn count_increasing_sums(nums: Vec<i32>) -> i32 {
    let sums =
        nums.windows(3)
            .map(|win|
                match win {
                    [x, y, z] => x + y + z,
                    _ => panic!("unexpected value in window")
                }
            ).collect::<Vec<i32>>();
    count_increasing(sums)
}

fn count_increasing(nums: Vec<i32>) -> i32 {
    nums.windows(2)
        .map(|win|
            match win {
                [x, y] if x < y => 1,
                [x, y] if x >= y => 0,
                _ => panic!("invalid entry in nums"),
            }
        )
        .sum()
}

fn load_ints(filename: &str) -> Vec<i32> {
    let mut buf = String::new();
    File::open(filename)
        .expect("file not opened!")
        .read_to_string(&mut buf)
        .expect("could not read file!");
    buf.lines()
        .map(|x| x
            .splitn(1, " ")
            .next()
            .expect("could not split str")
            .parse()
            .expect("failed to parse int"))
        .collect::<Vec<i32>>()
}